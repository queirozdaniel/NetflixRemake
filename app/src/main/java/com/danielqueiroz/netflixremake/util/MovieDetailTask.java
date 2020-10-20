package com.danielqueiroz.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.danielqueiroz.netflixremake.model.Movie;
import com.danielqueiroz.netflixremake.model.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {

    private final WeakReference<Context> context;
    private ProgressDialog progressDialog;
    private MovieDetailLoader movieDetailLoader;

    public MovieDetailTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader){
        this.movieDetailLoader = movieDetailLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if (context != null)
            progressDialog = ProgressDialog.show(context, "Carregando", "", true);
    }

    @Override
    protected MovieDetail doInBackground(String... strings) {
        String url = strings[0];

        try{
            URL requestUrl = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();
            if (responseCode > 400) {
                throw  new IOException("Error na comunicação com servidor");
            }

            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String jsonAsString = toString(in);

            MovieDetail movieDetail = getMovieDetail(new JSONObject(jsonAsString));

            inputStream.close();
            return movieDetail;
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch ( IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private MovieDetail getMovieDetail(JSONObject jsonObject) throws JSONException{
        int id = jsonObject.getInt("id");
        String title = jsonObject.getString("title");
        String desc = jsonObject.getString("desc");
        String cast = jsonObject.getString("cast");
        String coverUrl = jsonObject.getString("cover_url");

        List<Movie> moviesSimilar = new ArrayList<>();
        JSONArray movieArray = jsonObject.getJSONArray("movie");
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieResult = movieArray.getJSONObject(i);
            int id_similar = movieResult.getInt("id");
            String cover_url_similar = movieResult.getString("cover_url");

            Movie similar = new Movie();
            similar.setId(id_similar);
            similar.setCoverUrl(cover_url_similar);
            moviesSimilar.add(similar);
        }
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setDesc(desc);
        movie.setCast(cast);
        movie.setCoverUrl(coverUrl);

        return new MovieDetail(movie, moviesSimilar);
    }

    private  String toString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = in.read(bytes)) > 0) {
            baos.write(bytes, 0 , lidos);
        }
        return new String(baos.toByteArray());
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        progressDialog.dismiss();

        if (movieDetailLoader != null)
            movieDetailLoader.onResult(movieDetail);
    }

    public interface MovieDetailLoader {
        void onResult(MovieDetail movieDetail);
    }

}

