package practice.design.principle.design_principle.lkp;

/**
 * @author：Administrator
 * @version:1.0
 */
public class Room {

	public int area;
	public int price;

	public Room(int area, int price) {
		this.area = area;
		this.price = price;
	}

	@Override
	public String toString() {
		return "Room{" +
				"area=" + area +
				", price=" + price +
				'}';
	}
}
