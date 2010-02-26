package net.wm161.microblog.lib;

import net.wm161.microblog.lib.APIRequest.ErrorType;


public interface ProgressHandler {
	public void updated(APIProgress progress);

	public void error(ErrorType errorType, String errorString);

	public void finished();

	public void starting();
}
