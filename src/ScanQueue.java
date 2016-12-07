import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ScanQueue {
    private int lineNumber;
    final ActorRef mySS;
    final ActorRef myBody;
    final ActorRef myBags;
    public ScanQueue(int lineNum, ActorRef theJail, ActorSystem system){
        lineNumber = lineNum;
        String line = Integer.toString(lineNum);
        mySS =  system.actorOf(Props.create(SecurityStation.class, lineNumber, theJail), "Security_Station_" + line);
        //Create the security station for this line

        myBody =  system.actorOf(Props.create(BodyScanner.class, lineNumber, mySS), "Body_Scanner_" + line);
        //Create the body scanner for this line

        myBags = system.actorOf(Props.create(BaggageScanner.class, lineNumber, mySS), "Baggage_Scanner_" + line);
        //Create the bag scanner for this line
    }

    public ActorRef getSS(){
        return mySS;
    }

    public ActorRef getBodyScanner(){
        return myBody;
    }
    public ActorRef getBagScanner(){
        return myBags;
    }
}
