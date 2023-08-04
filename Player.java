import java.util.*;
import java.io.*;
import java.math.*;

class Player {
   

    // Custom class to represent SNAFFLE entity
    static class SnaffleEntity {
        int entityId;
        int x;
        int y;

        SnaffleEntity(int entityId, int x, int y) {
            this.entityId = entityId;
            this.x = x;
            this.y = y;
        }
    }



    // Custom class to represent WIZARD entity
    static class WizardEntity {
        int entityId;
        int x;
        int y;
        int state;

        WizardEntity(int entityId, int x, int y, int state) {
            this.entityId = entityId;
            this.x = x;
            this.y = y;
            this.state = state;
        }
    }



    // Define a method to calculate the distance between two points
    static double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }



    public static void main(String args[]) {

        Scanner in = new Scanner(System.in);
        int myTeamId = in.nextInt();

        // ArrayLists to store entity information
        HashMap<Integer, String> entityTypeMap = new HashMap<>();   //entityID, entityType
        HashMap<Integer, Integer> xMap = new HashMap<>();           //entityID, x
        HashMap<Integer, Integer> yMap = new HashMap<>();           //entityID, y
        HashMap<Integer, Integer> vxMap = new HashMap<>();          //entityID, vx
        HashMap<Integer, Integer> vyMap = new HashMap<>();          //entityID, vy
        HashMap<Integer, Integer> stateMap = new HashMap<>();       //entityID, state

       
        

        // reference origin respewct to WIZARD 0
        int nearestX = 1;
        int nearestY = 5750;
        double minDistance = Double.MAX_VALUE;

        // Second reference origin respect to WIZARD 1
        int nearestX2 = 1;
        int nearestY2 = 1750;
        double minDistance2 = Double.MAX_VALUE;

        if(myTeamId == 1){
            nearestX=16000;
            nearestY=5750;
            nearestX2 = 16000;
            nearestY2 = 1750;
        }

        while (true) {
            int myScore = in.nextInt();
            int myMagic = in.nextInt();
            int opponentScore = in.nextInt();
            int opponentMagic = in.nextInt();
            int entities = in.nextInt();

 // ArrayLists to store SNAFFLE and WIZARD entities
        ArrayList<SnaffleEntity> snaffles = new ArrayList<>();      //entityID, x, y
        ArrayList<WizardEntity> wizards = new ArrayList<>();        //entityID, x, y

            // Clear the ArrayLists and HashMaps for each loop iteration
            entityTypeMap.clear();
            xMap.clear();
            yMap.clear();
            vxMap.clear();
            vyMap.clear();
            stateMap.clear();
            snaffles.clear();
            wizards.clear();


            int loopCount =0;
            for (int i = 0; i < entities; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int vx = in.nextInt();
                int vy = in.nextInt();
                int state = in.nextInt();

                // Store entity information in the respective ArrayLists
                entityTypeMap.put(entityId, entityType);
                xMap.put(entityId, x);
                yMap.put(entityId, y);
                vxMap.put(entityId, vx);
                vyMap.put(entityId, vy);
                stateMap.put(entityId, state);

                // Add SNAFFLE entity to the ArrayList called snaffles, in format: entityID, x, y
                if (entityType.equals("SNAFFLE")) {
                    snaffles.add(new SnaffleEntity(entityId, x, y));
                }

                // Add WIZARD entity to the ArrayList called wizards, in format: entityID, x, y
                if (entityType.equals("WIZARD")) {
                    wizards.add(new WizardEntity(entityId, x, y, state));
                }
 
                // Check if the entity is SNAFFLE
                if (entityType.equals("SNAFFLE")) {
                    if ( loopCount != 0 && (loopCount % 2) != 1) {
                        double distanceToOrigin = distance(x, y, wizards.get(0).x, wizards.get(0).y);

                        // Check if the current pair (x, y) is closer to the origin
                        if (distanceToOrigin < minDistance) {
                            minDistance = distanceToOrigin;
                            nearestX = x;
                            nearestY = y;
                        }
                    
                    }

                    if(loopCount ==0){                    
                        // Assume inside the loop, this is for WIZARD with entityID 1
                        // Calculate distance from the origin (nearestX2, nearestY2)
                        double distanceToOrigin2 = distance(x, y, nearestX2, nearestY2);

                        // Check if the current pair (x, y) is closer to the origin
                        if (distanceToOrigin2 < minDistance2) {
                            minDistance2 = distanceToOrigin2;
                            nearestX2 = x;
                            nearestY2 = y;
                        }
                    }
                    loopCount++;
                }
                
                                    

                


            }
        
        

            

            System.err.println("Wizards: " + wizards.size());


            for (int i = 0; i < 2; i++) {
                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                // Edit this line to indicate the action for each wizard (0 ≤ thrust ≤ 150, 0 ≤ power ≤ 500)
                // i.e.: "MOVE x y thrust" or "THROW x y power"
    WizardEntity w = wizards.get(i); 

    // Check if the WIZARD is holding a SNAFFLE
    if (w.state == 1) {
        // If WIZARD with entityID 0 is holding a SNAFFLE, throw it toward (16000, 3750)
        //if (i == 0) {
        if (myTeamId == 0) {
            System.out.println("THROW 16000 3750 500");
            // Update the nearestX and nearestY variables for the thrown SNAFFLE
            nearestX = 16000;
            nearestY = 3750;
        } else if (myTeamId == 1) {
            System.out.println("THROW 1 3750 500");
            nearestX = 0;
            nearestY = 3750;
        }
        // }
        // If WIZARD with entityID 1 is holding a SNAFFLE, throw it toward (16000, 3750)
    } else {
        // If the WIZARD is not holding a SNAFFLE, move towards the nearest SNAFFLE
        int nearestSnaffleIndex = indexofnearest(snaffles, w);
        SnaffleEntity nearestSnaffle = snaffles.get(nearestSnaffleIndex);

        // If wizard has enough magic points and the distance to the nearest Snaffle is within range, cast ACCIO spell
        if (myMagic >= 15 && distance(nearestSnaffle.x, nearestSnaffle.y, w.x, w.y) <= 6000) {
            System.out.println("ACCIO " + nearestSnaffle.entityId);
        } else {
            // Move the wizard towards the nearest Snaffle
            System.out.println("MOVE " + nearestSnaffle.x + " " + nearestSnaffle.y + " " + 150);
        }
    }


            }
        }





        }

        public static int indexofnearest(ArrayList<SnaffleEntity> snaffles,WizardEntity wizard){
            ArrayList<Double> dist=new ArrayList<>();
            int index=0;


            for(int i =0;i<snaffles.size();i++){
                dist.add(distance(snaffles.get(i).x,snaffles.get(i).y,wizard.x,wizard.y));
            }

            dist.sort(Comparator.naturalOrder());

            for(int i =0;i<snaffles.size();i++){
                if(dist.get(0)==distance(snaffles.get(i).x,snaffles.get(i).y,wizard.x,wizard.y)){

                    index=i;
                }
            }


            return index;
        }
    }


        


        