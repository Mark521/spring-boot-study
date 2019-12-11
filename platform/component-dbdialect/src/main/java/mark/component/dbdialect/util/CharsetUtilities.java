package mark.component.dbdialect.util;

import java.text.Collator;
import java.util.*;

/**
 * 类名称： Charsets
 * 描述：
 * 
 * 创建：   liang, Nov 28, 2012 2:15:08 PM
 *
 * 修订历史：（降序）
 * BugID/BacklogID/描述  -  开发人员  日期
 * #  
 */
public class CharsetUtilities {
    
    private final static List<Charset> charsets = new LinkedList<Charset>();
    static{
        charsets.add(new Charset("US-ASCII",3,"iso-ir-6|ANSI_X3.4-1986|ISO_646.irv:1991|ASCII|ISO646-US|ANSI_X3.4-1968|us|IBM367|cp367|csASCII",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-1",4,"iso-ir-100|ISO_8859-1|ISO_8859-1:1987|latin1|l1|IBM819|CP819|csISOLatin1",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-2",5,"iso-ir-101|ISO_8859-2|ISO_8859-2:1987|latin2|l2|csISOLatin2",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-3",6,"iso-ir-109|ISO_8859-3|ISO_8859-3:1988|latin3|l3|csISOLatin3",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-4",7,"iso-ir-110|ISO_8859-4|ISO_8859-4:1988|latin4|l4|csISOLatin4",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-5",8,"iso-ir-144|ISO_8859-5|ISO_8859-5:1988|cyrillic|csISOLatinCyrillic",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-6",9,"iso-ir-127|ISO_8859-6|ISO_8859-6:1987|ECMA-114|ASMO-708|arabic|csISOLatinArabic",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-7",10,"iso-ir-126|ISO_8859-7|ISO_8859-7:1987|ELOT_928|ECMA-118|greek|greek8|csISOLatinGreek",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-8",11,"iso-ir-138|ISO_8859-8|ISO_8859-8:1988|hebrew|csISOLatinHebrew",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-9",12,"iso-ir-148|ISO_8859-9|ISO_8859-9:1989|latin5|l5|csISOLatin5",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO-8859-10",13,"iso-ir-157|l6|ISO_8859-10:1992|csISOLatin6|latin6",Charset.FLAG_COMMON));
        charsets.add(new Charset("ISO_6937-2-add",14,"iso-ir-142|csISOTextComm",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_X0201",15,"X0201|csHalfWidthKatakana",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_Encoding",16,"csJISEncoding",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Shift_JIS",17,"MS_Kanji|csShiftJIS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EUC-JP",18,"csEUCPkdFmtJapanese|Extended_UNIX_Code_Packed_Format_for_Japanese",Charset.FLAG_UNCOMMON));
//        charsets.add(new Charset("Extended_UNIX_Code_Fixed_Width_for_Japanese",19,"csEUCFixWidJapanese",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("BS_4730",20,"iso-ir-4|ISO646-GB|gb|uk|csISO4UnitedKingdom",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("SEN_850200_C",21,"iso-ir-11|ISO646-SE2|se2|csISO11SwedishForNames",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IT",22,"iso-ir-15|ISO646-IT|csISO15Italian",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ES",23,"iso-ir-17|ISO646-ES|csISO17Spanish",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("DIN_66003",24,"iso-ir-21|de|ISO646-DE|csISO21German",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NS_4551-1",25,"iso-ir-60|ISO646-NO|no|csISO60DanishNorwegian|csISO60Norwegian1",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NF_Z_62-010",26,"iso-ir-69|ISO646-FR|fr|csISO69French",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-10646-UTF-1",27,"csISO10646UTF1",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_646.basic:1983",28,"ref|csISO646basic1983",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("INVARIANT",29,"csINVARIANT",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_646.irv:1983",30,"iso-ir-2|irv|csISO2IntlRefVersion",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NATS-SEFI",31,"iso-ir-8-1|csNATSSEFI",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NATS-SEFI-ADD",32,"iso-ir-8-2|csNATSSEFIADD",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NATS-DANO",33,"iso-ir-9-1|csNATSDANO",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NATS-DANO-ADD",34,"iso-ir-9-2|csNATSDANOADD",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("SEN_850200_B",35,"iso-ir-10|FI|ISO646-FI|ISO646-SE|se|csISO10Swedish",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("KS_C_5601-1987",36,"iso-ir-149|KS_C_5601-1989|KSC_5601|korean|csKSC56011987",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-2022-KR",37,"csISO2022KR",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EUC-KR",38,"csEUCKR",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-2022-JP",39,"csISO2022JP",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-2022-JP-2",40,"csISO2022JP2",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6220-1969-jp",41,"JIS_C6220-1969|iso-ir-13|katakana|x0201-7|csISO13JISC6220jp",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6220-1969-ro",42,"iso-ir-14|jp|ISO646-JP|csISO14JISC6220ro",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("PT",43,"iso-ir-16|ISO646-PT|csISO16Portuguese",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("greek7-old",44,"iso-ir-18|csISO18Greek7Old",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("latin-greek",45,"iso-ir-19|csISO19LatinGreek",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NF_Z_62-010_(1973)",46,"iso-ir-25|ISO646-FR1|csISO25French",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Latin-greek-1",47,"iso-ir-27|csISO27LatinGreek1",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_5427",48,"iso-ir-37|csISO5427Cyrillic",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6226-1978",49,"iso-ir-42|csISO42JISC62261978",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("BS_viewdata",50,"iso-ir-47|csISO47BSViewdata",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("INIS",51,"iso-ir-49|csISO49INIS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("INIS-8",52,"iso-ir-50|csISO50INIS8",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("INIS-cyrillic",53,"iso-ir-51|csISO51INISCyrillic",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_5427:1981",54,"iso-ir-54|ISO5427Cyrillic1981|csISO54271981",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_5428:1980",55,"iso-ir-55|csISO5428Greek",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("GB_1988-80",56,"iso-ir-57|cn|ISO646-CN|csISO57GB1988",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("GB_2312-80",57,"iso-ir-58|chinese|csISO58GB231280",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NS_4551-2",58,"ISO646-NO2|iso-ir-61|no2|csISO61Norwegian2",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("videotex-suppl",59,"iso-ir-70|csISO70VideotexSupp1",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("PT2",60,"iso-ir-84|ISO646-PT2|csISO84Portuguese2",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ES2",61,"iso-ir-85|ISO646-ES2|csISO85Spanish2",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("MSZ_7795.3",62,"iso-ir-86|ISO646-HU|hu|csISO86Hungarian",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6226-1983",63,"iso-ir-87|x0208|JIS_X0208-1983|csISO87JISX0208",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("greek7",64,"iso-ir-88|csISO88Greek7",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ASMO_449",65,"ISO_9036|arabic7|iso-ir-89|csISO89ASMO449",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("iso-ir-90",66,"csISO90",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6229-1984-a",67,"iso-ir-91|jp-ocr-a|csISO91JISC62291984a",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6229-1984-b",68,"iso-ir-92|ISO646-JP-OCR-B|jp-ocr-b|csISO92JISC62991984b",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6229-1984-b-add",69,"iso-ir-93|jp-ocr-b-add|csISO93JIS62291984badd",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6229-1984-hand",70,"iso-ir-94|jp-ocr-hand|csISO94JIS62291984hand",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6229-1984-hand-add",71,"iso-ir-95|jp-ocr-hand-add|csISO95JIS62291984handadd",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_C6229-1984-kana",72,"iso-ir-96|csISO96JISC62291984kana",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_2033-1983",73,"iso-ir-98|e13b|csISO2033",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ANSI_X3.110-1983",74,"iso-ir-99|CSA_T500-1983|NAPLPS|csISO99NAPLPS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("T.61-7bit",75,"iso-ir-102|csISO102T617bit",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("T.61-8bit",76,"T.61|iso-ir-103|csISO103T618bit",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ECMA-cyrillic",77,"iso-ir-111|KOI8-E|csISO111ECMACyrillic",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CSA_Z243.4-1985-1",78,"iso-ir-121|ISO646-CA|csa7-1|csa71|ca|csISO121Canadian1",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CSA_Z243.4-1985-2",79,"iso-ir-122|ISO646-CA2|csa7-2|csa72|csISO122Canadian2",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CSA_Z243.4-1985-gr",80,"iso-ir-123|csISO123CSAZ24341985gr",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-6-E",81,"csISO88596E|ISO_8859-6-E",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-6-I",82,"csISO88596I|ISO_8859-6-I",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("T.101-G2",83,"iso-ir-128|csISO128T101G2",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-8-E",84,"csISO88598E|ISO_8859-8-E",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-8-I",85,"csISO88598I|ISO_8859-8-I",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CSN_369103",86,"iso-ir-139|csISO139CSN369103",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JUS_I.B1.002",87,"iso-ir-141|ISO646-YU|js|yu|csISO141JUSIB1002",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IEC_P27-1",88,"iso-ir-143|csISO143IECP271",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JUS_I.B1.003-serb",89,"iso-ir-146|serbian|csISO146Serbian",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JUS_I.B1.003-mac",90,"macedonian|iso-ir-147|csISO147Macedonian",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("greek-ccitt",91,"iso-ir-150|csISO150|csISO150GreekCCITT",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("NC_NC00-10:81",92,"cuba|iso-ir-151|ISO646-CU|csISO151Cuba",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_6937-2-25",93,"iso-ir-152|csISO6937Add",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("GOST_19768-74",94,"ST_SEV_358-88|iso-ir-153|csISO153GOST1976874",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_8859-supp",95,"iso-ir-154|latin1-2-5|csISO8859Supp",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO_10367-box",96,"iso-ir-155|csISO10367Box",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("latin-lap",97,"lap|iso-ir-158|csISO158Lap",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("JIS_X0212-1990",98,"x0212|iso-ir-159|csISO159JISX02121990",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("DS_2089",99,"DS2089|ISO646-DK|dk|csISO646Danish",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("us-dk",100,"csUSDK",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("dk-us",101,"csDKUS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("KSC5636",102,"ISO646-KR|csKSC5636",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UNICODE-1-1-UTF-7",103,"csUnicode11UTF7",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-2022-CN",104,"csISO2022CN",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-2022-CN-EXT",105,"csISO2022CNEXT",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-13",109,"csISO885913",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-14",110,"iso-ir-199|ISO_8859-14:1998|ISO_8859-14|latin8|iso-celtic|l8|csISO885914",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-15",111,"ISO_8859-15|Latin-9|csISO885915",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-8859-16",112,"iso-ir-226|ISO_8859-16:2001|ISO_8859-16|latin10|l10|csISO885916",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("GB18030",114,"csGB18030",Charset.FLAG_COMMON));
        charsets.add(new Charset("OSD_EBCDIC_DF04_15",115,"csOSDEBCDICDF0415",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("OSD_EBCDIC_DF03_IRV",116,"csOSDEBCDICDF03IRV",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("OSD_EBCDIC_DF04_1",117,"csOSDEBCDICDF041",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-11548-1",118,"ISO_11548-1|ISO_TR_11548-1|csISO115481",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("KZ-1048",119,"STRK1048-2002|RK1048|csKZ1048",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-10646-UCS-2",1000,"csUnicode",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-10646-UCS-4",1001,"csUCS4",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-10646-UCS-Basic",1002,"csUnicodeASCII",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-10646-Unicode-Latin1",1003,"csUnicodeLatin1|ISO-10646",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-10646-J-1",1004,"csUnicodeJapanese",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-Unicode-IBM-1261",1005,"csUnicodeIBM1261",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-Unicode-IBM-1268",1006,"csUnicodeIBM1268",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-Unicode-IBM-1276",1007,"csUnicodeIBM1276",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-Unicode-IBM-1264",1008,"csUnicodeIBM1264",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("ISO-Unicode-IBM-1265",1009,"csUnicodeIBM1265",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UNICODE-1-1",1010,"csUnicode11",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("SCSU",1011,"csSCSU",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UTF-7",1012,"csUTF7",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UTF-16BE",1013,"csUTF16BE",Charset.FLAG_COMMON));
        charsets.add(new Charset("UTF-16LE",1014,"csUTF16LE",Charset.FLAG_COMMON));
        charsets.add(new Charset("UTF-16",1015,"csUTF16",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CESU-8",1016,"csCESU8|csCESU-8",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UTF-32",1017,"csUTF32",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UTF-32BE",1018,"csUTF32BE",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UTF-32LE",1019,"csUTF32LE",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("BOCU-1",1020,"csBOCU1|csBOCU-1",Charset.FLAG_UNCOMMON));
//        charsets.add(new Charset("ISO-8859-1-Windows-3.0-Latin-1",2000,"csWindows30Latin1",Charset.FLAG_UNCOMMON));
//        charsets.add(new Charset("ISO-8859-1-Windows-3.1-Latin-1",2001,"csWindows31Latin1",Charset.FLAG_UNCOMMON));
//        charsets.add(new Charset("ISO-8859-2-Windows-Latin-2",2002,"csWindows31Latin2",Charset.FLAG_UNCOMMON));
//        charsets.add(new Charset("ISO-8859-9-Windows-Latin-5",2003,"csWindows31Latin5",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("hp-roman8",2004,"roman8|r8|csHPRoman8",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Adobe-Standard-Encoding",2005,"csAdobeStandardEncoding",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Ventura-US",2006,"csVenturaUS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Ventura-International",2007,"csVenturaInternational",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("DEC-MCS",2008,"dec|csDECMCS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM850",2009,"cp850|850|csPC850Multilingual",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("PC8-Danish-Norwegian",2012,"csPC8DanishNorwegian",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM862",2013,"cp862|862|csPC862LatinHebrew",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("PC8-Turkish",2014,"csPC8Turkish",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM-Symbols",2015,"csIBMSymbols",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM-Thai",2016,"csIBMThai",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("HP-Legal",2017,"csHPLegal",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("HP-Pi-font",2018,"csHPPiFont",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("HP-Math8",2019,"csHPMath8",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Adobe-Symbol-Encoding",2020,"csHPPSMath",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("HP-DeskTop",2021,"csHPDesktop",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Ventura-Math",2022,"csVenturaMath",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Microsoft-Publishing",2023,"csMicrosoftPublishing",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Windows-31J",2024,"csWindows31J",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("GB2312",2025,"csGB2312",Charset.FLAG_COMMON));
        charsets.add(new Charset("Big5",2026,"csBig5",Charset.FLAG_COMMON));
        charsets.add(new Charset("macintosh",2027,"mac|csMacintosh",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM037",2028,"cp037|ebcdic-cp-us|ebcdic-cp-ca|ebcdic-cp-wt|ebcdic-cp-nl|csIBM037",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM038",2029,"EBCDIC-INT|cp038|csIBM038",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM273",2030,"CP273|csIBM273",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM274",2031,"EBCDIC-BE|CP274|csIBM274",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM275",2032,"EBCDIC-BR|cp275|csIBM275",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM277",2033,"EBCDIC-CP-DK|EBCDIC-CP-NO|csIBM277",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM278",2034,"CP278|ebcdic-cp-fi|ebcdic-cp-se|csIBM278",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM280",2035,"CP280|ebcdic-cp-it|csIBM280",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM281",2036,"EBCDIC-JP-E|cp281|csIBM281",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM284",2037,"CP284|ebcdic-cp-es|csIBM284",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM285",2038,"CP285|ebcdic-cp-gb|csIBM285",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM290",2039,"cp290|EBCDIC-JP-kana|csIBM290",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM297",2040,"cp297|ebcdic-cp-fr|csIBM297",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM420",2041,"cp420|ebcdic-cp-ar1|csIBM420",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM423",2042,"cp423|ebcdic-cp-gr|csIBM423",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM424",2043,"cp424|ebcdic-cp-he|csIBM424",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM437",2011,"cp437|437|csPC8CodePage437",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM500",2044,"CP500|ebcdic-cp-be|ebcdic-cp-ch|csIBM500",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM851",2045,"cp851|851|csIBM851",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM852",2010,"cp852|852|csPCp852",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM855",2046,"cp855|855|csIBM855",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM857",2047,"cp857|857|csIBM857",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM860",2048,"cp860|860|csIBM860",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM861",2049,"cp861|861|cp-is|csIBM861",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM863",2050,"cp863|863|csIBM863",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM864",2051,"cp864|csIBM864",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM865",2052,"cp865|865|csIBM865",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM868",2053,"CP868|cp-ar|csIBM868",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM869",2054,"cp869|869|cp-gr|csIBM869",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM870",2055,"CP870|ebcdic-cp-roece|ebcdic-cp-yu|csIBM870",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM871",2056,"CP871|ebcdic-cp-is|csIBM871",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM880",2057,"cp880|EBCDIC-Cyrillic|csIBM880",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM891",2058,"cp891|csIBM891",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM903",2059,"cp903|csIBM903",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM904",2060,"cp904|904|csIBBM904",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM905",2061,"CP905|ebcdic-cp-tr|csIBM905",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM918",2062,"CP918|ebcdic-cp-ar2|csIBM918",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM1026",2063,"CP1026|csIBM1026",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-AT-DE",2064,"csIBMEBCDICATDE",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-AT-DE-A",2065,"csEBCDICATDEA",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-CA-FR",2066,"csEBCDICCAFR",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-DK-NO",2067,"csEBCDICDKNO",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-DK-NO-A",2068,"csEBCDICDKNOA",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-FI-SE",2069,"csEBCDICFISE",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-FI-SE-A",2070,"csEBCDICFISEA",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-FR",2071,"csEBCDICFR",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-IT",2072,"csEBCDICIT",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-PT",2073,"csEBCDICPT",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-ES",2074,"csEBCDICES",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-ES-A",2075,"csEBCDICESA",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-ES-S",2076,"csEBCDICESS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-UK",2077,"csEBCDICUK",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("EBCDIC-US",2078,"csEBCDICUS",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("UNKNOWN-8BIT",2079,"csUnknown8BiT",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("MNEMONIC",2080,"csMnemonic",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("MNEM",2081,"csMnem",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("VISCII",2082,"csVISCII",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("VIQR",2083,"csVIQR",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("KOI8-R",2084,"csKOI8R",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("HZ-GB-2312",2085,"",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM866",2086,"cp866|866|csIBM866",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM775",2087,"cp775|csPC775Baltic",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("KOI8-U",2088,"csKOI8U",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM00858",2089,"CCSID00858|CP00858|PC-Multilingual-850+euro|csIBM00858",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM00924",2090,"CCSID00924|CP00924|ebcdic-Latin9--euro|csIBM00924",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01140",2091,"CCSID01140|CP01140|ebcdic-us-37+euro|csIBM01140",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01141",2092,"CCSID01141|CP01141|ebcdic-de-273+euro|csIBM01141",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01142",2093,"CCSID01142|CP01142|ebcdic-dk-277+euro|ebcdic-no-277+euro|csIBM01142",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01143",2094,"CCSID01143|CP01143|ebcdic-fi-278+euro|ebcdic-se-278+euro|csIBM01143",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01144",2095,"CCSID01144|CP01144|ebcdic-it-280+euro|csIBM01144",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01145",2096,"CCSID01145|CP01145|ebcdic-es-284+euro|csIBM01145",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01146",2097,"CCSID01146|CP01146|ebcdic-gb-285+euro|csIBM01146",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01147",2098,"CCSID01147|CP01147|ebcdic-fr-297+euro|csIBM01147",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01148",2099,"CCSID01148|CP01148|ebcdic-international-500+euro|csIBM01148",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("IBM01149",2100,"CCSID01149|CP01149|ebcdic-is-871+euro|csIBM01149",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Big5-HKSCS",2101,"csBig5HKSCS",Charset.FLAG_COMMON));
        charsets.add(new Charset("IBM1047",2102,"",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("PTCP154",2103,"csPTCP154|PT154|CP154|Cyrillic-Asian|csPTCP154",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("Amiga-1251",2104,"Ami1251|Amiga1251|Ami-1251|csAmiga1251",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("KOI7-switched",2105,"csKOI7switched",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("BRF",2106,"csBRF",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("TSCII",2107,"csTSCII",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CP51932",2108,"csCP51932",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-874",2109,"cswindows874",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1250",2250,"cswindows1250",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1251",2251,"cswindows1251",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1252",2252,"cswindows1252",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1253",2253,"cswindows1253",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1254",2254,"cswindows1254",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1255",2255,"cswindows1255",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1256",2256,"cswindows1256",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1257",2257,"cswindows1257",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("windows-1258",2258,"cswindows1258",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("TIS-620",2259,"csTIS620",Charset.FLAG_UNCOMMON));
        charsets.add(new Charset("CP50220",2260,"",Charset.FLAG_UNCOMMON));
        
        sortBliangisplayName(charsets);
        charsets.add(0,new Charset("GBK",113,"CP936|MS936|windows-936|csGBK",Charset.FLAG_COMMON));
        charsets.add(1,new Charset("UTF-8",106,"csUTF8",Charset.FLAG_COMMON));
    }
    
    /**
     * 标准的字符编码全集
     * http://www.iana.org/assignments/character-sets
     * @return 
     */
    public static List<Charset> getFullCharsets(){
        return new ArrayList<Charset>(charsets);
    }
    
    /**
     * 常用的字符集
     * @return 
     */
    public static List<Charset> getCommonCharsets(){
        List<Charset> list = new ArrayList<Charset>();
        for (Charset charset : charsets) {
            if(charset.getFlag() == Charset.FLAG_COMMON){
                list.add(charset);
            }
        }
        return list;
    }
    /**
     * 不常用的字符集
     * @return 
     */
    public static List<Charset> getUncommonCharsets(){
        List<Charset> list = new ArrayList<Charset>();
        for (Charset charset : charsets) {
            if(charset.getFlag() == Charset.FLAG_UNCOMMON){
                list.add(charset);
            }
        }
        return list;
    }
    
    /**
     * 按名称排序
     * @param charsets 
     */
    public static void sortBliangisplayName(List<Charset> charsets){
        Collections.sort(charsets,new Comparator<Charset>(){
            Collator collator = Collator.getInstance(Locale.CHINESE);
            public int compare(Charset o1, Charset o2) {
                return collator.compare(o1.getDisplayName(), o2.getDisplayName());
            }
        });
    }
    
    /**
     * 按MIBEnum排序
     * @param charsets 
     */
    public static void sortByMIBEnum(List<Charset> charsets){
        Collections.sort(charsets,new Comparator<Charset>(){
            public int compare(Charset o1, Charset o2) {
                return o1.getMibEnum()-o2.getMibEnum();
            }
        });
    }
}
