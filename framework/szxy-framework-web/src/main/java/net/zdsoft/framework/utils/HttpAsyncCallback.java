package net.zdsoft.framework.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.concurrent.FutureCallback;

public class HttpAsyncCallback implements FutureCallback<HttpResponse> {
	@Override
	public void cancelled() {
	}

	@Override
	public void completed(HttpResponse response) {
		HttpClientUtils.closeQuietly(response);
	}

	@Override
	public void failed(Exception arg0) {

	}

}
