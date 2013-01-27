package com.whiterabbit.postmanlibsample.com.whiterabbit.postmanlibsample.commands;

import android.content.Context;
import android.os.Parcel;
import com.whiterabbit.postman.commands.RestServerStrategy;
import com.whiterabbit.postman.exceptions.ResultParseException;
import com.whiterabbit.postmanlibsample.StoreUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: fedepaol
 * Date: 12/18/12
 * Time: 12:41 AM
 */
public class TwitterGetLatestTweetStrategy implements RestServerStrategy{
    private static final String url = "https://api.twitter.com/1.1/statuses/home_timeline.json";

    public TwitterGetLatestTweetStrategy(){

    }

    @Override
    public String getOAuthSigner() {
        return "Twitter";
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public Verb getVerb() {
        return Verb.GET;
    }

    @Override
    public void processHttpResult(Response result, Context context) throws ResultParseException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(result.getBody());
            for (JsonNode tweet : root){
                String text = tweet.path("text").toString();
                String user = tweet.path("user").path("name").toString();

                StoreUtils.setLatestTweet(String.format("%s -> %s", user, text), context);
            }
        } catch (IOException e) {
            throw new ResultParseException("Failed to parse response");
        }

    }

    @Override
    public void addParamsToRequest(OAuthRequest request) {
        request.addQuerystringParameter("count", "1");
    }

    @Override
    public int describeContents() {
        return 0;  //TODO Autogenerated
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }


    public static final Creator<TwitterGetLatestTweetStrategy> CREATOR
            = new Creator<TwitterGetLatestTweetStrategy>() {
        public TwitterGetLatestTweetStrategy createFromParcel(Parcel in) {
            return new TwitterGetLatestTweetStrategy(in);
        }

        public TwitterGetLatestTweetStrategy[] newArray(int size) {
            return new TwitterGetLatestTweetStrategy[size];
        }
    };


    public TwitterGetLatestTweetStrategy(Parcel in){
    }

}