

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

  public Jail(Integer SSNum) {
     jailed = new ArrayList<Passenger>();
     this.SSNum = SSNum;
  }
  public void onReceive(Object message) {
    if(message instanceof Passenger) {
      onReceive((Passenger)message);
    }
  }

  private void onReceive(Passenger passenger) {
    System.out.println(passenger.getName() + " has entered the jail.");
    jailed.add(passenger);
  }
}
