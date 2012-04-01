
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ZeroShortner zeromk  = new ZeroShortner();
		zeromk.shortLink("http://google.com", Constants.JSON_FORMAT, null);
		System.out.println("Response:" + zeromk.getStringResponse());
		//.out.println("JSON:" + zeromk.getLinkObject().getShortLinkUrl());
		zeromk.shortLink("https://twitter.com", Constants.XML_FORMAT, "dsad");
		System.out.println("XML:" + zeromk.getLinkObject().getShortLinkUrl());
		zeromk.shortLink("https://facebook.com", Constants.PLAIN_TEXT_FORMAT, "p");
		System.out.println("PLAIN TEXT:" + zeromk.getLinkObject().getShortLinkUrl());
		byte[] qr = zeromk.shortLink("https://github.com", Constants.QR_FORMAT, null);
		System.out.println("QR byte array length:" + qr.length);
		
		AsyncZeroMkShortner async = new AsyncZeroMkShortner("http://google.com", Constants.JSON_FORMAT, null);
		async.setOnShortningComplete(new ShorteningListener(){

			@Override
			public void onShortCompleted(byte[] data) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onShortLinkCompleted(ZeroLink link) {
				System.out.print("Async: " + link.getShortLinkUrl());
			}});
		async.setOnErrorListener(new ErrorListener(){

			@Override
			public void onError(int errorId, String errorMsg) {
				// TODO Auto-generated method stub
				
			}});
		async.execute();
		
	}

}
