import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;


public class Main {
    public static void main(String[] args) {
        // Start the JADE runtime
        Runtime rt = Runtime.instance();

        // Create main container profile
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN, "true");

        // Create main container
        ContainerController cc = rt.createMainContainer(p);

        try {
            // Launch VisualizerAgent
            AgentController visualizer = cc.createNewAgent(
                    "visualizer",
                    "agents.VisualizerAgent",
                    null);
            visualizer.start();

            // Launch RecommenderAgent
            AgentController recommender = cc.createNewAgent(
                    "recommender",
                    "agents.RecommenderAgent",
                    null);
            recommender.start();

            // Launch TransformerAgent
            AgentController transformer = cc.createNewAgent(
                    "transformer",
                    "agents.TransformerAgent",
                    null);
            transformer.start();

            // Launch IngestorAgent
            AgentController ingestor = cc.createNewAgent(
                    "ingestor",
                    "agents.IngestorAgent",
                    null);
            ingestor.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }
}
