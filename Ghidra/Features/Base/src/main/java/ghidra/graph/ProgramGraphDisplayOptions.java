/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.graph;

import static ghidra.docking.util.Theming.*;
import static ghidra.graph.ProgramGraphType.*;
import static ghidra.service.graph.VertexShape.*;

import ghidra.framework.plugintool.PluginTool;
import ghidra.service.graph.GraphDisplayOptions;
import ghidra.service.graph.VertexShape;
import ghidra.util.WebColors;

/** 
 * {@link GraphDisplayOptions} for {@link ProgramGraphType}
 */
public class ProgramGraphDisplayOptions extends GraphDisplayOptions {

	/**
	 * constructor
	 * @param graphType the specific ProgramGraphType subclass for these options
	 * @param tool if non-null, will load values from tool options
	 */
	public ProgramGraphDisplayOptions(ProgramGraphType graphType, PluginTool tool) {
		super(graphType, tool);
	}

	@Override
	protected void initializeDefaults() {
		setDefaultVertexShape(ELLIPSE);
		setDefaultVertexColor(themed(WebColors.RED));
		setDefaultEdgeColor(themed(WebColors.RED));
		setFavoredEdgeType(FALL_THROUGH);

		configureVertexType(BODY, RECTANGLE, themed(WebColors.BLUE));
		configureVertexType(ENTRY, TRIANGLE_DOWN, themed(WebColors.DARK_ORANGE));
		configureVertexType(EXIT, TRIANGLE_UP, themed(WebColors.DARK_MAGENTA));
		configureVertexType(SWITCH, DIAMOND, themed(WebColors.DARK_CYAN));
		configureVertexType(EXTERNAL, RECTANGLE, themed(WebColors.DARK_GREEN));
		configureVertexType(BAD, ELLIPSE, themed(WebColors.RED));
		configureVertexType(DATA, ELLIPSE, themed(WebColors.PINK));
		configureVertexType(ENTRY_NEXUS, ELLIPSE, themed(WebColors.WHEAT));
		configureVertexType(INSTRUCTION, VertexShape.HEXAGON, themed(WebColors.BLUE));
		configureVertexType(STACK, RECTANGLE, themed(WebColors.GREEN));

		configureEdgeType(ENTRY_EDGE, themed(WebColors.GRAY));
		configureEdgeType(FALL_THROUGH, themed(WebColors.BLUE));
		configureEdgeType(UNCONDITIONAL_JUMP, themed(WebColors.DARK_GREEN));
		configureEdgeType(UNCONDITIONAL_CALL, themed(WebColors.DARK_ORANGE));
		configureEdgeType(TERMINATOR, themed(WebColors.PURPLE));
		configureEdgeType(JUMP_TERMINATOR, themed(WebColors.PURPLE));
		configureEdgeType(INDIRECTION, themed(WebColors.PINK));

		configureEdgeType(CONDITIONAL_JUMP, themed(WebColors.DARK_GOLDENROD));
		configureEdgeType(CONDITIONAL_CALL, themed(WebColors.DARK_ORANGE));
		configureEdgeType(CONDITIONAL_TERMINATOR, themed(WebColors.PURPLE));
		configureEdgeType(CONDITIONAL_CALL_TERMINATOR, themed(WebColors.PURPLE));

		configureEdgeType(COMPUTED_JUMP, themed(WebColors.CYAN));
		configureEdgeType(COMPUTED_CALL, themed(WebColors.CYAN));
		configureEdgeType(COMPUTED_CALL_TERMINATOR, themed(WebColors.PURPLE));

		configureEdgeType(CONDITIONAL_COMPUTED_CALL, themed(WebColors.CYAN));
		configureEdgeType(CONDITIONAL_COMPUTED_JUMP, themed(WebColors.CYAN));

		configureEdgeType(CALL_OVERRIDE_UNCONDITIONAL, themed(WebColors.RED));
		configureEdgeType(JUMP_OVERRIDE_UNCONDITIONAL, themed(WebColors.RED));
		configureEdgeType(CALLOTHER_OVERRIDE_CALL, themed(WebColors.RED));
		configureEdgeType(CALLOTHER_OVERRIDE_JUMP, themed(WebColors.RED));

		configureEdgeType(READ, themed(WebColors.GREEN));
		configureEdgeType(WRITE, themed(WebColors.RED));
		configureEdgeType(READ_WRITE, themed(WebColors.DARK_GOLDENROD));
		configureEdgeType(UNKNOWN_DATA, themed(WebColors.BLACK));
		configureEdgeType(EXTERNAL_REF, themed(WebColors.PURPLE));

		configureEdgeType(READ_INDIRECT, themed(WebColors.DARK_GREEN));
		configureEdgeType(WRITE_INDIRECT, themed(WebColors.DARK_RED));
		configureEdgeType(READ_WRITE_INDIRECT, themed(WebColors.BROWN));
		configureEdgeType(DATA_INDIRECT, themed(WebColors.DARK_ORANGE));

		configureEdgeType(PARAM, themed(WebColors.CYAN));
		configureEdgeType(THUNK, themed(WebColors.BLUE));

	}
}
