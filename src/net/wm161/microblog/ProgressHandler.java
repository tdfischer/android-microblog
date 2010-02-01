package net.wm161.microblog;

import net.wm161.microblog.APIRequest.ErrorType;

public interface ProgressHandler {
	public void updated(APIProgress progress);

	public void error(ErrorType errorType, String errorString);

	public void finished();

	public void starting();
}
