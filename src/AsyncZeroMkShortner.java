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
			Runner runner = new Runner(0);
			runner.start();
		}
		public void setOnShortningComplete(ShorteningListener listener)
		{
			mShorteningListener = listener;
			
		}
		
		
		
		private class Runner extends Thread
		{
			private int mOperation;
			private ZeroLink mDeleteLink;
			public Runner(int what)
			{
				mOperation = what;
			}
			public Runner(ZeroLink l)
			{
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