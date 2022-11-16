package com.mraof.minestuck.entity.underling;

import com.mraof.minestuck.entity.IEntityMultiPart;
import com.mraof.minestuck.entity.ai.CustomMeleeAttackGoal;
import com.mraof.minestuck.alchemy.GristHelper;
import com.mraof.minestuck.alchemy.GristSet;
import com.mraof.minestuck.alchemy.GristType;
import com.mraof.minestuck.player.Echeladder;
import com.mraof.minestuck.util.AnimationUtil;
import com.mraof.minestuck.util.MSSoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;

public class BasiliskEntity extends UnderlingEntity implements IAnimatable
{
	private final BasiliskPartEntity[] parts;
	private final BasiliskPartEntity head;
	private final BasiliskPartEntity body;
	private final BasiliskPartEntity tail;
	private final BasiliskPartEntity tailEnd;
	
	public BasiliskEntity(EntityType<? extends BasiliskEntity> type, Level level)
	{
		super(type, level, 5);
		
		this.head = new BasiliskPartEntity(this, "head", 2.3F, 2.3F);
		this.body = new BasiliskPartEntity(this, "body", 2.8F, 2.2F);
		this.tail = new BasiliskPartEntity(this, "tail", 2.0F, 2.0F);
		this.tailEnd = new BasiliskPartEntity(this, "tailEnd", 1.7F, 1.7F);
		parts = new BasiliskPartEntity[]{this.head, this.body, this.tail, this.tailEnd};
		this.noCulling = true;
	}
	
	public static AttributeSupplier.Builder basiliskAttributes()
	{
		return UnderlingEntity.underlingAttributes().add(Attributes.MAX_HEALTH, 85)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6).add(Attributes.MOVEMENT_SPEED, 0.25)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}
	
	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new SlowAttackWhenInRangeGoal<>(this, 4, 10));
		this.goalSelector.addGoal(2, new ZeroMovementDuringAttack<>(this));
		this.goalSelector.addGoal(3, new MoveToTargetGoal(this, 1F, false));
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_AMBIENT.get();
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{
		return MSSoundEvents.ENTITY_BASILISK_HURT.get();
	}
	
	@Override
	protected SoundEvent getDeathSound()
	{
		return MSSoundEvents.ENTITY_BASILISK_DEATH.get();
	}
	
	@Override
	public GristSet getGristSpoils()
	{
		return GristHelper.generateUnderlingGristDrops(this, damageMap, 6);
	}
	
	@Override
	protected int getVitalityGel()
	{
		return random.nextInt(3) + 4;
	}
	
	@Override
	protected void onGristTypeUpdated(GristType type)
	{
		super.onGristTypeUpdated(type);
		applyGristModifier(Attributes.MAX_HEALTH, 20 * type.getPower(), AttributeModifier.Operation.ADDITION);
		applyGristModifier(Attributes.ATTACK_DAMAGE, 2.7 * type.getPower(), AttributeModifier.Operation.ADDITION);
		this.xpReward = (int) (6 * type.getPower() + 4);
	}
	
	@Override
	public void baseTick()
	{
		super.baseTick();
		this.updatePartPositions();
	}
	
	@Override
	public boolean attackEntityFromPart(Entity entityPart, DamageSource source, float damage)
	{
		return this.hurt(source, damage);
	}
	
	@Override
	protected void doPush(Entity par1Entity)
	{
		if(par1Entity != this.tail)
			super.doPush(par1Entity);
	}
	
	@Override
	public void absMoveTo(double par1, double par3, double par5, float par7, float par8)
	{
		super.absMoveTo(par1, par3, par5, par7, par8);
		this.updatePartPositions();
	}
	
	@Override
	public void updatePartPositions()
	{
		if(tail == null)
			return;
		float f1 = this.yRotO + (this.getYRot() - this.yRotO);
		double tailPosX = (this.getX() + Math.sin(f1 / 180.0 * Math.PI) * tail.getBbWidth());
		double tailPosZ = (this.getZ() + -Math.cos(f1 / 180.0 * Math.PI) * tail.getBbWidth());
		
		tail.absMoveTo(tailPosX, this.getY(), tailPosZ, this.getYRot(), this.getXRot());
	}
	
	@Override
	public void addPart(Entity entityPart, int id)
	{
		this.tail = (UnderlingPartEntity) entityPart;
	}
	
	@Override
	public void onPartDeath(Entity entityPart, int id)
	{
	
	}
	
	@Override
	public void die(DamageSource cause)
	{
		super.die(cause);
		Entity entity = cause.getEntity();
		if(this.dead && !this.level.isClientSide)
		{
			computePlayerProgress((int) (30 + 2.4 * getGristType().getPower())); //most basilisks stop giving xp at rung 32
			firstKillBonus(entity, (byte) (Echeladder.UNDERLING_BONUS_OFFSET + 2));
		}
	}
	
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}
	
	public void checkDespawn()
	{
	}
	
	@Override
	public boolean isPickable()
	{
		return false;
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		// save the current part positions
		Vector3d[] positions = new Vector3d[this.parts.length];
		for(int j = 0; j < this.parts.length; ++j)
		{
			positions[j] = new Vector3d(this.parts[j].getX(), this.parts[j].getY(), this.parts[j].getZ());
		}
		
		float bodyAngle = this.yBodyRot * ((float) Math.PI / 180F);
		float xOffset = MathHelper.sin(bodyAngle);
		float zOffset = -MathHelper.cos(bodyAngle);
		
		// update the body parts based on the body rotation + apply natural offsets
		this.updatePart(this.body, 0, 0, 0);
		this.updatePart(this.head, xOffset * -2.5, 0.3, zOffset * -2.5);
		this.updatePart(this.tail, xOffset * 2.5, 0, zOffset * 2.5);
		this.updatePart(this.tailEnd, xOffset * 4.5, 1, zOffset * 4.5);
		
		// sets various mysterious params - used to sync client server stuff
		for(int l = 0; l < this.parts.length; ++l)
		{
			this.parts[l].xo = positions[l].x;
			this.parts[l].yo = positions[l].y;
			this.parts[l].zo = positions[l].z;
			this.parts[l].xOld = positions[l].x;
			this.parts[l].yOld = positions[l].y;
			this.parts[l].zOld = positions[l].z;
		}
	}
	
	private void updatePart(BasiliskPartEntity part, double xOffset, double yOffset, double zOffset)
	{
		part.setPos(this.getX() + xOffset, this.getY() + yOffset, this.getZ() + zOffset);
	}
	
	@Override
	public boolean isMultipartEntity()
	{
		return true;
	}
	
	@Override
	public net.minecraftforge.entity.PartEntity<?>[] getParts()
	{
		return this.parts;
	}
	
	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(AnimationUtil.createAnimation(this, "walkAnimation", 0.5, BasiliskEntity::walkAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "deathAnimation", 1, BasiliskEntity::deathAnimation));
		data.addAnimationController(AnimationUtil.createAnimation(this, "swingAnimation", 1, BasiliskEntity::swingAnimation));
	}
	
	private static PlayState walkAnimation(AnimationEvent<BasiliskEntity> event)
	{
		if(!event.isMoving())
		{
			return PlayState.STOP;
		}
		
		if(event.getAnimatable().isAggressive())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("run", true));
			return PlayState.CONTINUE;
		} else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
			return PlayState.CONTINUE;
		}
	}
	
	private static PlayState deathAnimation(AnimationEvent<BasiliskEntity> event)
	{
		if(event.getAnimatable().dead)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("die", false));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}
	
	private static PlayState swingAnimation(AnimationEvent<BasiliskEntity> event)
	{
		if(event.getAnimatable().isAttacking())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", false));
			return PlayState.CONTINUE;
		}
		event.getController().markNeedsReload();
		return PlayState.STOP;
	}
}