

/**
 * The board class represents the game board in the Catan game
 * This mainly stores the board elements and provides access to them through
 * getter methods.
 * It also manages the turns of players.
 * 
 * @author Nitya Patel, Ranica Chawla, Raadhikka Gupta, Krisha Patel
 */
public class Board {
    private Tile[] tiles; // Tiles on board
    private Node[] nodes; // Nodes on board
    private Road[] roads; // Roads on board

    private GameLogger logger; // Logger for game events
    private RobberActionsManager robberManager; // Robber on board

    /**
     * Constructs a Board with tiles, nodes and roads
     * 
     * @param tiles array of Tile
     * @param nodes array of Node
     * @param roads array of Roads
     */
    public Board(GameLogger logger) {
        // Initialize the board with 19 tiles and 54 nodes
        this.tiles = new Tile[19];
        this.nodes = new Node[54];
        this.roads = new Road[72];

        this.logger = logger; // Initialize the logger

        // Loop to create nodes and assign IDs
        for (int i = 0; i < 54; i++) {
            nodes[i] = new Node(i);
        }

        assignResources();
        connectNodesToTiles();
        connectAdjacentNodes();
    }

    /**
     * To set the robber manager up
     * 
     * @param robberManager robber manager
     */
    public void setRobberManager(RobberActionsManager robberManager) {
        this.robberManager = robberManager;
    }

    /**
     * Assign resources to each tile
     */
    private void assignResources() {
        Resources[] resourceOrder = {
                Resources.LUMBER,
                Resources.GRAIN,
                Resources.BRICK,
                Resources.ORE,
                Resources.WOOL,
                Resources.WOOL,
                Resources.WOOL,
                Resources.GRAIN,
                Resources.ORE,
                Resources.LUMBER,
                Resources.ORE,
                Resources.GRAIN,
                Resources.LUMBER,
                Resources.BRICK,
                Resources.BRICK,
                Resources.GRAIN,
                Resources.NOTHING,
                Resources.LUMBER,
                Resources.WOOL
        };

        int[] tokenNum = { 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11, 0 };
        // Loop to create tiles and assign IDs and token numbers
        for (int i = 0; i < 19; i++) {
            tiles[i] = new Tile(resourceOrder[i], i, tokenNum[i]);
        }
    }

    /**
     * Connects each tile to its nodes
     */
    private void connectNodesToTiles() {
        // Define a mapping of the tiles to their corresponding nodes
        int[][] tileNodeMapping = {
                { 0, 1, 2, 3, 4, 5 },
                { 3, 2, 9, 8, 7, 6 },
                { 3, 12, 11, 10, 9, 4 },
                { 5, 4, 15, 14, 13, 12 },
                { 0, 13, 17, 18, 16, 5 },
                { 0, 1, 17, 21, 19, 20 },
                { 2, 1, 6, 23, 22, 21 },
                { 7, 8, 27, 26, 25, 24 },
                { 9, 10, 29, 28, 27, 8 },
                { 11, 32, 31, 30, 29, 10 },
                { 14, 34, 33, 32, 11, 12 },
                { 37, 36, 35, 34, 15, 14 },
                { 39, 38, 35, 13, 15, 18 },
                { 42, 41, 38, 16, 18, 40 },
                { 44, 41, 19, 17, 16, 43 },
                { 45, 44, 20, 19, 46, 47 },
                { 49, 20, 21, 22, 47, 48 },
                { 49, 22, 23, 52, 51, 50 },
                { 23, 6, 7, 24, 53, 52 }
        };

        // Loop to connect each tile to its corresponding nodes based on the mapping
        for (int i = 0; i < 19; i++) {
            // Create an array to hold the nodes for the current tile
            Node[] tileNodes = new Node[6];
            // Loop to assign the nodes to the tile based on the mapping
            for (int j = 0; j < 6; j++) {
                // Get the node index from the mapping and assign the corresponding node to the
                // tile
                tileNodes[j] = nodes[tileNodeMapping[i][j]];
            }
            tiles[i].setNodes(tileNodes);
        }
    }

    /**
     * Connects each node on the board to its adjacent nodes.
     */
    private void connectAdjacentNodes() {
        // Define adjacency for each node
        int[][] adjacency = {
                { 1, 5, 17 }, // 0
                { 0, 2, 21 }, // 1
                { 1, 3, 6 }, // 2
                { 2, 4, 9 }, // 3
                { 3, 5, 12 }, // 4
                { 0, 4, 13 }, // 5
                { 7, 2, 23 }, // 6
                { 6, 8, 24 }, // 7
                { 7, 9, 27 }, // 8
                { 3, 8, 10 }, // 9
                { 9, 11, 29 }, // 10
                { 10, 12, 32 }, // 11
                { 4, 11, 14 }, // 12
                { 5, 15, 18 }, // 13
                { 12, 15, 34 }, // 14
                { 13, 14, 35 }, // 15
                { 17, 41, 18 }, // 16
                { 0, 16, 19 }, // 17
                { 13, 16, 38 }, // 18
                { 17, 20, 44 }, // 19
                { 19, 21, 47 }, // 20
                { 1, 20, 22 }, // 21
                { 21, 23, 49 }, // 22
                { 6, 22, 52 }, // 23
                { 7, 25, 53 }, // 24
                { 24, 26 }, // 25
                { 25, 27 }, // 26
                { 8, 26, 28 }, // 27
                { 27, 29 }, // 28
                { 10, 28, 30 }, // 29
                { 29, 31 }, // 30
                { 30, 32 }, // 31
                { 11, 31, 33 }, // 32
                { 32, 34 }, // 33
                { 14, 33, 36 }, // 34
                { 15, 37, 39 }, // 35
                { 34, 37 }, // 36
                { 35, 36 }, // 37
                { 18, 39, 42 }, // 38
                { 38, 35 }, // 39
                { 41, 42 }, // 40
                { 40, 43 }, // 41
                { 38, 40 }, // 42
                { 41, 44 }, // 43
                { 19, 43, 45 }, // 44
                { 44, 46 }, // 45
                { 45, 47 }, // 46
                { 20, 46, 48 }, // 47
                { 47, 49 }, // 48
                { 48, 50, 22 }, // 49
                { 49, 51 }, // 50
                { 50, 52 }, // 51
                { 23, 51, 53 }, // 52
                { 52, 24 } // 53
        };

        // Add adjacent nodes
        for (int i = 0; i < adjacency.length; i++) {
            for (int neighborIndex : adjacency[i]) {
                nodes[i].addAdjacentNode(nodes[neighborIndex]);
            }
        }
    }

    /**
     * @return all tiles on the board
     */
    public Tile[] getTile() {
        return this.tiles;
    }

    /**
     * @return all nodes on the board
     */
    public Node[] getNode() {
        return this.nodes;
    }

    /**
     * @return all roads on the board
     */
    public Road[] getRoad() {
        return this.roads;
    }

    private boolean settlementDistance(Node node) {
        for (Node n : node.getAdjacentNodes()) {
            if (n.isOccupied()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a road connecting two nodes can be built by a player.
     * The proposed road or building is already connected with player's exiting
     * road.
     * 
     * @param player the player attempting to build
     * @param n1     the first node of proposed road
     * @param n2     second node of the proposed road
     * @return true if the road can be built, false otherwise
     */
    private boolean isRoadConnected(Player player, Node n1, Node n2) {
        // Check if either node has a player owned building
        if ((n1.isOccupied() && n1.getBuilding().getOwner() == player)
                || (n2.isOccupied() && n2.getBuilding().getOwner() == player))
            return true;

        // check if the road is connected to an exiting player owned road
        for (Road road : roads) {
            if (road != null && road.getOwner() == player && (road.isConnected(n1) || road.isConnected(n2))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a new road to the board's road array
     * add the road to the empty slot.
     * 
     * @param road the road to add
     */
    private void addRoadToBoard(Road road) {
        for (int i = 0; i < roads.length; i++) {
            if (roads[i] == null) {
                roads[i] = road;
                break;
            }
        }
    }


    /**
     * Places initial settlements for all players
     */
    public void placeInitialSettlements(Player[] players) {
        // Nodes for settlements
        Node[] settlementNodes = { nodes[0], nodes[5], nodes[10], nodes[38], nodes[1], nodes[15], nodes[11],
                nodes[35] };

        // Node pairs for roads, two per player
        Node[][] roadPairs = {
                { nodes[0], nodes[1] },  //r
                { nodes[1], nodes[2]  },  

                {  nodes[5], nodes[13]},  //b
                { nodes[13], nodes[15]  },

                { nodes[10], nodes[11] }, //o
                { nodes[11], nodes[12] }, 

                { nodes[38], nodes[39] }, //w
                { nodes[39], nodes[35]} 
        };

        // Place settlements exactly as before
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];

            Node firstSettlementNode = settlementNodes[i];
            Node secondSettlementNode = settlementNodes[i + players.length];

            // First settlement
            Settlement s1 = new Settlement(player);
            firstSettlementNode.setBuilding(s1);
            player.addBuilding(s1);

            // Second settlement
            Settlement s2 = new Settlement(player);
            secondSettlementNode.setBuilding(s2);
            player.addBuilding(s2);

            logger.log(player.getPlayerId(),
                    "placed initial settlements at Node " + firstSettlementNode.getNodeId() +
                            " and Node " + secondSettlementNode.getNodeId());

            // Each player gets 2 roads (index = i*2 and i*2+1)
            for (int j = i * 2; j < i * 2 + 2; j++) {
                Node[] pair = roadPairs[j];
                Road road = new Road(pair, player);
                addRoadToBoard(road);
                player.addRoad();

                logger.log(player.getPlayerId(),
                        "built road between Node " + pair[0].getNodeId() + " and Node " + pair[1].getNodeId());
            }
        }
    }
    

    /**
     * Helper method
     * Attempts to build a road for the player.
     * two nodes and checks if the road can be legally built.
     * adds road to the board if successful and in the player's building
     * 
     * @param player  the player
     * @param nodeId1
     * @param nodeId2
     */
    public void buildRoad(Player player, int nodeId1, int nodeId2) {

        Node n1 = nodes[nodeId1];
        Node n2 = nodes[nodeId2];

        if (!player.hasResources(Resources.BRICK, 1) ||
                !player.hasResources(Resources.LUMBER, 1)) {

            logger.log(player.getPlayerId(), "Not enough resources for road");
            return;
        }

        if (n1 != n2 && isRoadConnected(player, n1, n2)) {

            player.removeResource(Resources.BRICK, 1);
            player.removeResource(Resources.LUMBER, 1);

            Road road = new Road(new Node[] { n1, n2 }, player);
            addRoadToBoard(road);
            player.addRoad();

            logger.log(player.getPlayerId(),
                    "built Road between Node " + nodeId1 + " and Node " + nodeId2);

        } else {
            logger.log(player.getPlayerId(), "Invalid road placement");
        }
    }

    /**
     * Helper method
     * Attempts to unbuild a road for the player.
     * two nodes and checks if the road can be legally built.
     * removes road from the board if successful and in the player's building
     * 
     * @param player  the player
     * @param nodeId1
     * @param nodeId2
     */
    public void removeRoad(Player player, int nodeId1, int nodeId2) {
        Node n1 = nodes[nodeId1];
        Node n2 = nodes[nodeId2];
    
        for (Road road : roads) {
            if (road != null &&
                road.getOwner() == player &&
                road.isConnected(n1) &&
                road.isConnected(n2)) {
                    
                    // Remove road from the board
                    for (int i = 0; i < roads.length; i++) {
                        if (roads[i] == road) {
                            roads[i] = null;
                            break;
                        }
                    }
    
                // Update player
                player.removeRoad();
    
                // Refund resources
                player.addResource(Resources.BRICK, 1);
                player.addResource(Resources.LUMBER, 1);
    
                logger.log(player.getPlayerId(),
                    "UNDO: removed Road between Node " + nodeId1 + " and Node " + nodeId2);
    
                return;
            }
        }
    }

    /**
     * helper method
     * Attempts to build a City for the player by upgrading the settlement.
     * Checks the node if it is settlement.
     * upgrades the settlement into city to the player's buildings and the board
     * 
     * @param player
     * @param nodeId
     */
    public void buildCity(Player player, int nodeId) {

        Node n = nodes[nodeId];

        if (!player.hasResources(Resources.ORE, 3) ||
                !player.hasResources(Resources.GRAIN, 2)) {

            logger.log(player.getPlayerId(), "Not enough resources for city");
            return;
        }

        if (n.isOccupied() &&
                n.getBuilding() instanceof Settlement &&
                n.getBuilding().getOwner() == player) {

            player.removeResource(Resources.ORE, 3);
            player.removeResource(Resources.GRAIN, 2);

            City city = new City(player);
            n.setBuilding(city);
            player.addBuilding(city);

            logger.log(player.getPlayerId(),
                    "upgraded to City at Node " + nodeId);

        } else {
            logger.log(player.getPlayerId(), "City upgrade failed");
        }
    }

    public void removeCity(Player player, int nodeId) {
        Node n = nodes[nodeId];
    
        if (n.isOccupied() &&
            n.getBuilding() instanceof City &&
            n.getBuilding().getOwner() == player) {
    
            // Remove city
            Building city = n.getBuilding();
            player.removeBuilding(city);
    
            // Replace with settlement
            Settlement settlement = new Settlement(player);
            n.setBuilding(settlement);
            player.addBuilding(settlement);
    
            // Refund resources
            player.addResource(Resources.ORE, 3);
            player.addResource(Resources.GRAIN, 2);
    
            logger.log(player.getPlayerId(),
                "UNDO: unbuilt City to Settlement at Node " + nodeId);
        }
    }

    /**
     * Attempts to build a Settlement for the player.
     * Checks the node if it is empty.
     * adds the settlement to the player's buildings and the board
     * 
     * @param player
     * @nodeId
     */
    public void buildSettlement(Player player, int nodeId) {

        Node n = nodes[nodeId];

        if (!player.hasResources(Resources.BRICK, 1) ||
                !player.hasResources(Resources.LUMBER, 1) ||
                !player.hasResources(Resources.WOOL, 1) ||
                !player.hasResources(Resources.GRAIN, 1)) {

            logger.log(player.getPlayerId(), "Not enough resources for settlement");
            return;
        }

        if (!n.isOccupied() && !settlementDistance(n)) {

            player.removeResource(Resources.BRICK, 1);
            player.removeResource(Resources.LUMBER, 1);
            player.removeResource(Resources.WOOL, 1);
            player.removeResource(Resources.GRAIN, 1);

            Settlement settlement = new Settlement(player);
            n.setBuilding(settlement);
            player.addBuilding(settlement);

            logger.log(player.getPlayerId(),
                    "built Settlement at Node " + nodeId);

        } else {
            logger.log(player.getPlayerId(), "Invalid settlement location");
        }
    }

    public void removeSettlement(Player player, int nodeId) {
        Node n = nodes[nodeId];
    
        if (n.isOccupied() &&
            n.getBuilding() instanceof Settlement &&
            n.getBuilding().getOwner() == player) {
    
            // Remove building from node
            Building building = n.getBuilding();
            n.setBuilding(null);
    
            // Remove from player's building list
            player.removeBuilding(building);
    
            // Give resources back
            player.addResource(Resources.BRICK, 1);
            player.addResource(Resources.LUMBER, 1);
            player.addResource(Resources.WOOL, 1);
            player.addResource(Resources.GRAIN, 1);
    
            logger.log(player.getPlayerId(),
                "UNDO: removed Settlement at Node " + nodeId);
        }
    }

      /**
     * Registers all tiles with the dice so they recive dice roll notifications
     * Also sets up the robber and logger for each tile
     * 
     * @param dice The MultiDice object that tiles will observe
     */
    public void registerTileWithDice(MultiDice dice) {
        for (Tile t : tiles) {
            t.setRobberManager(robberManager);
            t.setLogger(logger);
            dice.addObserver(t);
        }
    }
}
