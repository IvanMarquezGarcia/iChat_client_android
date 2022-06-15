package com.ivan.iChat.ichat_android.utils;

import java.util.HashMap;

public class Core {

    public static HashMap<String, String> strToHashMap(String strData) {
        if (strData != null) {
            if (strData.contains("{"))
                strData = strData.replace("{", "");
            if (strData.contains("}"))
                strData = strData.replace("}", "");

            String[] aux = strData.split(", ");
            HashMap<String, String> data = new HashMap<String, String>();

            for (String s : aux) {
                String[] pair = s.split("=");
                System.out.println(s);
                data.put(pair[0], pair[1]);
            }

            return data;
        } else
            return null;
    }


//    public static String translate(String text, String from, String to) {
//        String translation = text;
//        System.out.println(from + " -> " + to);
//
//        if (!from.equals(to)) {
//            String langPair = from + "|" + to;
//
//            try {
//                String url = "https://api.mymemory.translated.net/get?q=" +
//                        URLEncoder.encode(text, "UTF8") + "&langpair=" +
//                        URLEncoder.encode(langPair, "UTF8");
//                HttpRequest request = java.net.http.HttpRequest.newBuilder()
//                        .uri(URI.create(url)).build();
//                java.net.http.HttpClient httpClient = java.net.http.HttpClient.newHttpClient();
//                HttpResponse<String> response = httpClient.send(request, java.net.http.HttpResponse.BodyHandlers .ofString());
//                JSONObject json = new JSONObject(response.body());
//                json = (JSONObject) json.get("responseData");
//                translation = json.get("translatedText").toString();
//            }
//            catch (UnsupportedEncodingException e) { e.printStackTrace(); }
//            catch (InterruptedException e) { e.printStackTrace(); }
//            catch (IOException e) { e.printStackTrace(); }
//        }
//
//        return translation;
//    }

}
