# Tehtävä 2
2.  Yksittäisten resurssien rajat voidaan merkata monella eri tavalla ilman yksittäisiä olioita. Koodatessa esimerkiksi HTML-kielellä, yksittäisten resurssien rajat voidaan merkata siten, että alkuun laitetaan merkki, ja lopetetaan jollain vastaavalla merkillä, joka kuitenkin eroaa aloitus merkistä, jotta tunnistetaan aloitus ja lopetuskin toisistaan. Esimerkiksi <Blaa blaa/> voisi olla yhden resurssin rajat, jotka on merkattu vastaanottajalle ilman olioita. 
Vastaavia resurssin merkkaus merkkejä voidaan hyödyntää myös esimerkiksi Javalla. Resursseja voidaan siinäkin merkitä monella tavalla. Luokkia ja metodeita, eli niiden aloitusta ja lopetusta merkataan ‘{}’ merkeillä. Pienempiin osiin pilkottuja resursseja voidaan jakaa esimerkiksi ‘()’ perus sulkumerkeillä tai ‘[]’ merkeillä. 
Voidaan myös erottaa koodista eroava viesti vastaavilla merkeillä, jottei ohjelma lue viestiä koodiksi. Aloittamalla rivin esim. ‘/***’ näillä kolmella merkillä, ja lisäämällä viestin loppuun ‘*/’ saadaan erotettua viesti koodin joukosta vastaanottajalle.
Esimerkkejä tavoista erotella resursseja:
<  />
( )
[ ]
{ }
/**      */
“ “
‘ ‘


# Tehtävä 3
3. Yleisimmät tietoturvaan liittyvät ongelmat näissä tapauksissa pystytään ratkaisemaan erilaisten salausalgoritmien kanssa. Ilman salausalgoritmeja on hankala todeta, että Mesh-viestit ovat luotettavia ja peukaloimattomia, ja että ne ovat tunnetulta lähettäjältä. Lähettäjä pystyy kommunikoimaan kaikkien Mesh-verkossa olevien laitteiden kanssa, jolloin olisi hyvä tietää viestin alkuperä. 
Jos ulkopuolinen osapuoli on päässyt verkkoon sisään tai käsiksi jotenkin, niin sillä voi mahdollisesti tehdä paljon. Tällöin voi muun muassa esiintyä muuna verkon jäsenenä tai mahdollisesti kaapata viestejä laitteiden välillä ja korruptoida niitä. Ulkopuolinen osapoli voi myös vain valvoa viestien sisältöä, koska kuuluu Mesh-verkkoon, niin näkee hänkin viestien sisällön. Voi hän sitten tehdä saaduillaan tiedoilla mitä lystää. Hän voi myös poistua verkosta ja liittyä verkkoon halutessaan jo tässä vaiheessa. 
Pahimmassa tapauksessa ulkopuolinen osapuoli esiintyy tunnettuna osapuolena ja tälle paljastetaan puoli vahingossa jotain, mikä antaa hänelle vaikka identiteetti varkauden mahdollisuudet. Tämänkin takia kannattaa käyttää esimerkiksi julkisen avaimen salausta tai digitaalista allekirjoitusta Mesh-verkossa. Näin saadaan selville oikea lähettäjä ja viestin oikeus lähes varmasti.
Digitaalinen allekirjoitus on julkiseen avaimeen perustuva allekirjoitus, jonka tarkoitus on varmentaa viestin sisältö ja lähettäjän henkilöllisyys. Matemaattinen allekirjoitus ei täsmää, jos joku muuttaa viestiä sen matkalla määränpäähän.
Julkisen avaimen salauksessa käytettyjä avaimia ei niiden laskutoimitusten vaativuuden takia periaatteessa voi päätellä toisistaan. Näin vain toinen avaimista täytyy pitää yksityisenä, kun toinen voidaan julkaista. Salaus on epäsymmetristä salausta.

