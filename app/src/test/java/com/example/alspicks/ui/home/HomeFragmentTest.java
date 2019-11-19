package com.example.alspicks.ui.home;

class HomeFragmentTest extends HomeFragment {

    /*
    @Test
    void createArtistSearchURLTest() throws Exception {
        try {
            createArtistSearchURL(-1);
            createArtistSearchURL(0);

        } catch (Exception e){
            Log.w("Artist id must be greater than zero!", e);
        }

        String url = createArtistSearchURL(92623);
        final URL urll = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) urll.openConnection();
        huc.setRequestMethod("HEAD");
        int responseCode = huc.getResponseCode();

        Assert.assertEquals(HttpURLConnection.HTTP_OK, responseCode);

    }

    @Test
    void searchAlbumsTest() throws IOException {
        ArrayList<String> urls = searchAlbums("album", "sleep dirt");
        for(String link : urls){
            final URL url = new URL(link);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            String result = huc.getContentType();
            if (!result.equals("image")){
                break;
            }
        }
    }*/


}