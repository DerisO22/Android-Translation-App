
<p><h1 align="center"><strong>Android Translation App 🚀 </strong></h1></p>

![Translation Image](https://t3.ftcdn.net/jpg/04/36/43/12/360_F_436431209_IrKCuIPj2FubrYDZpYLZPZxDShsqSrwa.jpg)

<p><h2 align="center"><strong>Welcome to my Translation App 👋</strong></h2></p>
<p align="center"><strong>My app allows users to translate text, speech, and images from a wide variety of different languages.<br>Navigate through the different windows by scrolling
or choosing a window from the home page.<br>Using Firebase ML and other language APIs, you will be able to accurately translate between 50+ langauges.</strong></p>
<br><br>

<p><h2 align="center"><strong>Language Translation Code 💻</strong></h2></p>

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

