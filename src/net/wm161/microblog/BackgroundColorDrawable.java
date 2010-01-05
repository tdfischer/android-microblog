package net.wm161.microblog;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;

public class BackgroundColorDrawable extends ShapeDrawable {

	private int m_a;
	private int m_r;
	private int m_g;
	private int m_b;
	private boolean m_rounded;
	
	public BackgroundColorDrawable(int color, boolean rounded) {
		//m_a = (color >> 16) & 0xff;
		m_a = 0xff;
		m_r = (color >> 8) & 0xff;
		m_g = (color >> 4) & 0xff;
		m_b = color & 0xff;
		m_rounded = rounded;
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (m_rounded) {
			Paint p = new Paint();
			p.setARGB(m_a, m_r, m_g, m_b);
			RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
			canvas.drawRoundRect(rect, 3, 3, p);
		} else {
			canvas.drawARGB(m_a, m_r, m_g, m_b);
		}
	}

	@Override
	public int getOpacity() {
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

	public int getColor() {
		return (m_a << 16) + (m_r << 8) + (m_g << 4) + m_b;
	}

}
