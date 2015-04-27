package com.bjcathay.android.async;

public interface ICallbackExecutor {

	void run(ICallback callback, Arguments arguments);

	public class CurrentThreadExecutor implements ICallbackExecutor {

		@Override
		public void run(ICallback callback, Arguments arguments) {
			callback.call(arguments);
		}

	}
}
