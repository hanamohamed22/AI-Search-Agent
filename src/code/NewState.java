package code;
import java.util.ArrayList;

public class NewState extends State {
	String grid; // a String which has the dimensions of the grid; Iron man x position,Iron man y
					// position; thanos x position, thanos y position; x and y positions of all
					// stones separated by commas; x and y positions of warriors separated by
					// commas; the damage; is thanos alive boolean

	public NewState(String grid) {

		this.grid = grid; // intialize grid
	}

	// the below code was not used as our hashset is of Strings not of States
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewState other = (NewState) obj;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
			return false;
		return true;
	}

}
