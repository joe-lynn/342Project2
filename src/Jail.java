

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
    if(--lineCount == 0){
      System.out.println(getSelf().path().name()+" received kill command, " + lineCount + " more until termination.");
      for (Passenger prisoner: jailed){
        System.out.println("Passenger " + prisoner.getName() + " has been transferred to permanent detention.");
      }
      System.out.println(getSelf().path().name()+" is terminating.");
      this.getContext().stop(getSelf());
      //Shutdown what remains of the system. Jail is the last to die.

      this.getContext().system().terminate();
      System.out.println("The system is terminating.");
    }

  }

  private void onReceive(Passenger passenger) {
    System.out.println("Passenger " + passenger.getName() + " has entered the jail.");
    jailed.add(passenger);
  }
}
