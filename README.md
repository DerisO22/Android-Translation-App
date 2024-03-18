
<h1 align="center">
    <img src="https://readme-typing-svg.herokuapp.com/?font=Righteous&size=35&center=true&vCenter=true&width=500&height=70&duration=4000&lines=Welcome!+👋;+Android+Translation+App+🚀;" />
</h1>


![Translation Image](https://t3.ftcdn.net/jpg/04/36/43/12/360_F_436431209_IrKCuIPj2FubrYDZpYLZPZxDShsqSrwa.jpg)

<p><h2 align="center"><strong>Welcome to my Translation App 👋</strong></h2></p>
<p align="center"><strong>My app allows users to translate text, speech, and images from a wide variety of different languages.<br>Navigate through the different windows by scrolling
or choosing a window from the home page.<br>Using Firebase ML and other language APIs, you will be able to accurately translate between 50+ langauges.</strong></p>
<br><br>

<p><h2 align="center"><strong>Translation Features Implementation 💻</strong></h2></p>

<p><strong>-  loadAvailableLanguages() populates the list with all languages that FireBase ML Kit provides</strong></p>

```ruby
private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for(String languageCode: languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            Log.d(TAG, "LoadAvailableLanguages: languageCode: "+languageCode);
            Log.d(TAG, "LoadAvailableLanguages: languageCode: "+languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);
        }
    }
```

<p><strong>-  Downloads the language model and outputs the translation, given the source and target language titles and codes.</strong></p>

```ruby
DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready, starting translate. . .");

                        progressDialog.show();
                            translator.translate(sourceLanguageText)
                                    .addOnSuccessListener(new OnSuccessListener<String>(){

                                        @Override
                                        public void onSuccess(String translatedText) {
                                            //Successfully Translated
                                            Log.d(TAG, "onSuccess: translatedText: "+translatedText);
                                            progressDialog.dismiss();
                                            destinationLanguageTv.setText(translatedText);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(textActivity.this, "Failed to translate due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
```

