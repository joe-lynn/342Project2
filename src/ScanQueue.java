import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ScanQueue {
    private int lineNumber;
    public ScanQueue(int lineNum, ActorRef theJail, ActorSystem system){
        lineNumber = lineNum;
        final ActorRef mySS =  system.actorOf(Props.create(SecurityStation.class, lineNumber, theJail));
        //Create the security station for this line
        final ActorRef myBody =  system.actorOf(Props.create(BodyScanner.class, lineNumber, mySS));
        //Create the body scanner for this line
        final ActorRef myBags = system.actorOf(Props.create(BaggageScanner.class, lineNumber, mySS));
        //Create the bag scanner for this line
    }
}
