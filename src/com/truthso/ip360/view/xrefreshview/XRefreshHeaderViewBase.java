package com.truthso.ip360.view.xrefreshview;


public interface XRefreshHeaderViewBase {
	/**
	 * 返回header内容的高度
	 * @return
	 */
	public int getHeaderContentHeight();
	public void setState(XRefreshViewState state);
	public void setRefreshTime(long lastRefreshTime);
}
