package com.mraof.minestuck.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * hopefully later down the line, someone will improve the code of pipe blocks to allow for
 * intersections or a separate intersection blockset will be made that actually work.
 * this class is only here because pipes are weird and can be waterlogged, unlike `RotatedPillarBlock`s
 */
public class PipeBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock
{
	public final ImmutableMap<Direction, VoxelShape> shape;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public PipeBlock(Properties properties)
	{
		super(properties);
		this.shape = MSBlockShapes.PIPE.createRotatedShapesAllDirections();
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		FluidState iFluidState = context.getLevel().getFluidState(context.getClickedPos());
		return super.getStateForPlacement(context).setValue(WATERLOGGED, iFluidState.getType() == Fluids.WATER);
	}
	
	@Override
	protected BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos)
	{
		if(stateIn.getValue(WATERLOGGED))
		{
			level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		
		return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
	}
	
	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return switch(state.getValue(AXIS)) // todo use three hard-coded shapes instead of generating all six directions
		{
			case X -> shape.get(Direction.EAST);
			case Y -> shape.get(Direction.UP);
			case Z -> shape.get(Direction.SOUTH);
		};
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		super.createBlockStateDefinition(builder);
		builder.add(WATERLOGGED);
	}
	
	@Override
	protected FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
}
