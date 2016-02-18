package practice.design.principle.design_principle.lkp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：Administrator
 * @version:1.0
 */
public class Mediator {
	List<Room> mRooms = new ArrayList<>();

	public Mediator() {
		for (int i = 0; i < 5; i++) {
			mRooms.add(new Room(14 + i, (14 + i) * 150));
		}
	}

	public Room rentOut(float area, float price) {
		for (Room room : mRooms) {
			if (isSuitable(area, price, room)) {
				return room;
			}
		}

		return null;
	}

	private boolean isSuitable(float area, float price, Room room) {
		return Math.abs(room.price - price) < Tenant.diffPrice &&
				Math.abs(room.area - area) < Tenant.diffArea;
	}
}
