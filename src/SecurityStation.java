

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

import java.util.Queue;
import java.util.LinkedList;

/**
* Security Station Actor aggregates results from the scanners to determine
* whether a Passenger has passed the security check.
*/
public class SecurityStation extends UntypedActor {

  private final int lineNumber;
  private final ActorRef jail;
  private final Queue<ScanReport> buffer;

  public SecurityStation(int line, ActorRef jail) {
    lineNumber = line;
    this.jail = jail;
    buffer = new LinkedList<ScanReport>();
  }

  public void onReceive(Object message) {
    if(message instanceof ScanReport) {
      onReceive((ScanReport)message);
    }
  }

  private void onReceive(ScanReport report) {
    //Scanners must necessarily process the passengers in order so it doesn't
    //matter which scanner gets further ahead than the other. As such we can
    //just keep a queue to track which one gets ahead and compare the head of
    //the queue to determine when a passenger has gone through both scanners.
    if(report.equals(buffer.peek())) { 
      ScanReport report2 = buffer.poll();
      //Tell the jail if the passenger fails either scan.
      if(!(report.getResult() && report2.getResult())) {
	jail.tell(report.getSubject(), getSelf());
      }
    } else {
      buffer.add(report);
    }
  }
}
