import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the current game state to a JSON file for visualization
 * 
 * @author Nitya Patel
 */

public class StateWriter {
    
    public static void writeState(Board board, RobberActionsManager robberManager) {
        if (board == null) return; //Exit if board is null
        
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n");
            
            // Robber
            Tile robber_coordinate = robberManager.getCurrentTile();
            sb.append("  \"robber_coordinate\": ").append(robber_coordinate != null ? robber_coordinate.getTileId() : -1).append(",\n");
            
            // Roads
            sb.append("  \"roads\": [\n");
            boolean first = true;
            for (Road road : board.getRoad()) {
                if (road != null) {
                    if (!first)  sb.append(",\n");
                    Node[] nodes = road.getConnectedNodes();
                    String color = getPlayerColor(road.getOwner().getPlayerId());
                    sb.append("    { \"a\": ").append(nodes[0].getNodeId())
                      .append(", \"b\": ").append(nodes[1].getNodeId())
                      .append(", \"owner\": \"").append(color).append("\" }");
                    first = false;
                }
            }
            sb.append("\n  ],\n");
            
            // Buildings
            sb.append("  \"buildings\": [\n");
            first = true;
            for (Node node : board.getNode()) {
                if (node.isOccupied()) {
                    if (!first)  sb.append(",\n");
                    
                    Building b = node.getBuilding();
                    String type;
                    String color = getPlayerColor(b.getOwner().getPlayerId());
                    
                    if (b instanceof City) {
                        type = "CITY";
                    } else {
                        type = "SETTLEMENT";
                    }
                    
                    sb.append("{ \"node\": ").append(node.getNodeId()).append(", \"owner\": \"").append(color).append("\"").append(", \"type\": \"").append(type).append("\" }");
                    
                    first = false;
                }
            }
            sb.append("\n  ]\n");
            
            sb.append("}");
            
            FileWriter file = new FileWriter("../visualize/state.json");
            file.write( sb.toString());
            file.close();
            
        } catch (IOException e) {
            System.out.println("Failed to save state: " + e.getMessage());
        }
    }
    
    /**
     * Converts player ID to color string
     * @param playerId The player ID (1-4)
     * @return Color string for visualization
     */
    private static String getPlayerColor(int playerId) {
        switch(playerId) {
            case 1: return "RED";
            case 2: return "BLUE";
            case 3: return "ORANGE";
            case 4: return "WHITE";
            default: return "GRAY";
        }
    }
}
