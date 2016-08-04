package com.truthso.ip360.view.xrefreshview;

public class XRefreshHolder {

	public int mOffsetY;

	public void move(int deltaY) {
		mOffsetY += deltaY;
	}

	public boolean hasHeaderPullDown() {
		return mOffsetY > 0;
	}

	public boolean hasFooterPullUp() {
		return mOffsetY < 0;
	}
}
