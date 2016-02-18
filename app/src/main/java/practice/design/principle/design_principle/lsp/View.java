package practice.design.principle.design_principle.lsp;

/**
 * @author：Administrator
 * @version:1.0
 */
public abstract class View {
	public abstract void draw();

	public void measure(int width, int height) {
//		测量视图大小
	}

	class Button extends View {

		@Override
		public void draw() {
//          绘制按钮
		}
	}

	class Textview extends View {

		@Override
		public void draw() {
//			绘制文本
		}
	}

}
