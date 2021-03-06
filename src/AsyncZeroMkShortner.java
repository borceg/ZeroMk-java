public class AsyncZeroMkShortner extends ZeroShortner
	{
		ZeroShortner mShort;
		private String mLink;
		private int mFormat;
		private String mAppendix;
		private ShorteningListener mShorteningListener;
		private byte[] mData;
		private ErrorListener mErrorListener;
		public AsyncZeroMkShortner(String apikey, String user,String link, int output, String nastavka)
		{
			super(apikey,user);
			mLink = link;
			mFormat = output;
			mAppendix = nastavka;
		}
		public void setOnErrorListener(ErrorListener listener)
		{
			mErrorListener = listener;
			super.setOnErrorListener(mErrorListener);
		}
		
		public AsyncZeroMkShortner(String link, int output, String nastavka)
		{
			super();
			mLink = link;
			mFormat = output;
			mAppendix = nastavka;
			
			
		}
		public AsyncZeroMkShortner()
		{
			super();
		}
		public void deleteAsync(ZeroLink l) //not finished, needs more working
		{
			Runner delete = new Runner(l);
			delete.start();
		}
		public void execute()
		{
			/*
			 * Method that will envoke the async task. Output of the operation can be consumed from the 
			 * onShortCompleted(byte[] data) or onShortLinkCompleted(ZeroLink link) 
			 */
			Runner runner = new Runner(0);
			runner.start();
		}
		public void setOnShortningComplete(ShorteningListener listener)
		{
			/*
			 * Sets ShorteningListener that will be envoked when async request is completed
			 * @params listener - ShorteningListener interface
			 */
			mShorteningListener = listener;
		}
		
		
		
		private class Runner extends Thread
		{
			private int mOperation;
			private ZeroLink mDeleteLink;
			public Runner(int what)
			{
				/*
				 * @params what - operation type. WARNING: This is not finished
				 */
				mOperation = what;
			}
			public Runner(ZeroLink l)
			{

				/*
				 * @params lin - ZeroLink object that will be used for deletion. WARNING: This is not finished, 
				 * as of deletion of the links in this library.
				 */
				mDeleteLink = l;
			}
		@Override
		public void run() {
			switch(mOperation)
			{
			case 0:
				mData=shortLink(mLink, mFormat, mAppendix);
				if(mShorteningListener!=null)
				{
					mShorteningListener.onShortCompleted(mData);
					mShorteningListener.onShortLinkCompleted(getLinkObject());
				}
				break;
			case 1:
				deleteLink(mDeleteLink);
				break;
			}
		
			super.run();
		}
		
	}
	}