

import akka.actor.UntypedActor;

import java.util.List;
import java.util.ArrayList;

/**
* Jail class for holding passengers who fail the security check.
* Maintains a list of passengers passed to it.
*/
public class Jail extends UntypedActor {
  private List<Passenger> jailed;
  //Jail needs to know the number of security stations feeding it
  private final int SSNum;
  private int lineCount;

  public Jail(Integer SSNum) {
     jailed = new ArrayList<Passenger>();
     this.SSNum = SSNum;
    lineCount = SSNum;
  }

  public void onReceive(Object message) {
    if(message instanceof Passenger) {
      onReceive((Passenger)message);
    } else if(message instanceof StopMessage){
      onReceive((StopMessage)message);
    }
  }

  private void onReceive(StopMessage killCommand){
    System.out.println("Jail received kill command from " + getSender().path().name());
    if(lineCount == 0){
      this.getContext().stop(getSelf());
    }
  }

  private void onReceive(Passenger passenger) {
    System.out.println("Passenger " + passenger.getName() + " has entered the jail.");
    jailed.add(passenger);
  }
}
