package practice.design.principle.design_principle.lkp;

/**
 * @author：Administrator
 * @version:1.0
 */
public class Tenant {
	public float roomArea;
	public float roomPrice;
	public static final float diffPrice = 100.0001f;
	public static final float diffArea = 0.00001f;

	public void rentRoom(Mediator mediator) {
		final Room room = mediator.rentOut(roomArea, roomPrice);
		if (room != null) {
			System.out.println("租到房了 " + room);
		} else {
			System.out.println("没有租到房 /(ㄒoㄒ)/~~");
		}
	}

}
