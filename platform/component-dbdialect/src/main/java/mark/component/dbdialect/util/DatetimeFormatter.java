package mark.component.dbdialect.util;

/**
 *
 * @author liang
 */
public class DatetimeFormatter {

    private static class Pair {

        public int first;
        public int second;

        public Pair() {
            this.first = -1;
            this.second = -1;
        }

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
    private final static double DAYS_PER_YEAR = 365.25;	/* assumes leap year every four years */

    private final static int MONTHS_PER_YEAR = 12;
    /*
     *	DAYS_PER_MONTH is very imprecise.  The more accurate value is
     *	365.2425/12 = 30.436875, or '30 days 10:29:06'.  Right now we only
     *	return an integral number of days, but someday perhaps we should
     *	also return a 'time' value to be used as well.	ISO 8601 suggests
     *	30 days.
     */
    private final static int DAYS_PER_MONTH = 30;	/* assumes exactly 30 days per month */

    private final static int HOURS_PER_DAY = 24;	/* assume no daylight savings time changes */

    /*
     *	This doesn't adjust for uneven daylight savings time intervals or leap
     *	seconds, and it crudely estimates leap years.  A more accurate value
     *	for days per years is 365.2422.
     */
    private final static int SECS_PER_YEAR = (36525 * 864);	/* avoid floating-point computation */

    private final static int SECS_PER_DAY = 86400;
    private final static int SECS_PER_HOUR = 3600;
    private final static int SECS_PER_MINUTE = 60;
    private final static int MINS_PER_HOUR = 60;
    private final static long USECS_PER_DAY = 86400000000L;
    private final static long USECS_PER_HOUR = 3600000000L;
    private final static int USECS_PER_MINUTE = 60000000;
    private final static int USECS_PER_SEC = 1000000;
    //KeyWord Index (ascii from position 32 (' ') to 126 (~))
    private final static int KeyWord_INDEX_SIZE = ('~' - ' ');
    private final static int DCH_MAX_ITEM_SIZ = 9;		//* max julian day		*/
    private final static int NUM_MAX_ITEM_SIZ = 8;		//* roman number (RN has 15 chars)	*/
    //
    private final static int NODE_TYPE_END = 1;
    private final static int NODE_TYPE_ACTION = 2;
    private final static int NODE_TYPE_CHAR = 3;
    //
    private final static int SUFFTYPE_PREFIX = 1;
    private final static int SUFFTYPE_POSTFIX = 2;
    //
    private final static int CLOCK_24_HOUR = 0;
    private final static int CLOCK_12_HOUR = 1;
    //
    public final static String[] localized_abbrev_days = {"", "", "", "", "", "", ""};
    public final static String[] localized_full_days = {"", "", "", "", "", "", ""};
    public final static String[] localized_abbrev_months = {"", "", "", "", "", "", "", "", "", "", "", ""};
    public final static String[] localized_full_months = {"", "", "", "", "", "", "", "", "", "", "", ""};
    public final static int[][] day_tab = {
        {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 0},
        {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, 0}
    };
    public final static String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public final static String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday"};
    //Full months
    public final String months_full[] = {
        "January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"
    };
    public final String days_short[] = {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };
    public final static String months_low[] = {"jan", "feb", "mar", "apr", "may", "jun",
        "jul", "aug", "sep", "oct", "nov", "dec"};
    public final static String days_low[] = {"sunday", "monday", "tuesday", "wednesday",
        "thursday", "friday", "saturday"};
    public final String months_full_low[] = {
        "january", "february", "march", "april", "may", "june", "july",
        "august", "september", "october", "november", "december"
    };
    public final String days_short_low[] = {
        "sun", "mon", "tue", "wed", "thu", "fri", "sat"
    };

    /*
     * AD / BC strings for seq_search.
     *
     * These are given in two variants, a long form with periods and a standard
     * form without.
     *
     * The array is laid out such that matches for AD have an even index, and
     * matches for BC have an odd index.  So the boolean value for BC is given by
     * taking the array index of the match, modulo 2.
     */
    private final static String A_D_STR = "A.D.";
    private final static String a_d_STR = "a.d.";
    private final static String AD_STR = "AD";
    private final static String ad_STR = "ad";
    private final static String B_C_STR = "B.C.";
    private final static String b_c_STR = "b.c.";
    private final static String BC_STR = "BC";
    private final static String bc_STR = "bc";
    private final static String adbc_strings[] = {ad_STR, bc_STR, AD_STR, BC_STR};
    private final static String adbc_strings_long[] = {a_d_STR, b_c_STR, A_D_STR, B_C_STR};
    /* ----------
     * AD / BC
     * ----------
     *	There is no 0 AD.  Years go from 1 BC to 1 AD, so we make it
     *	positive and map year == -1 to year zero, and shift all negative
     *	years up one.  For interval years, we just return the year.
     */

    private int ADJUST_YEAR(int year, boolean is_interval) {
        return is_interval ? year : (year <= 0 ? -(year - 1) : year);
    }
    /*
     * AM / PM strings for seq_search.
     *
     * These are given in two variants, a long form with periods and a standard
     * form without.
     *
     * The array is laid out such that matches for AM have an even index, and
     * matches for PM have an odd index.  So the boolean value for PM is given by
     * taking the array index of the match, modulo 2.
     */
    private final static String A_M_STR = "A.M.";
    private final static String a_m_STR = "a.m.";
    private final static String AM_STR = "AM";
    private final static String am_STR = "am";
    private final static String P_M_STR = "P.M.";
    private final static String p_m_STR = "p.m.";
    private final static String PM_STR = "PM";
    private final static String pm_STR = "pm";
    private final static String ampm_strings[] = {am_STR, pm_STR, AM_STR, PM_STR};
    private final static String ampm_strings_long[] = {a_m_STR, p_m_STR, A_M_STR, P_M_STR};
    /* ----------
     * Months in roman-numeral
     * (Must be in reverse order for seq_search (in FROM_CHAR), because
     *	'VIII' must have higher precedence than 'V')
     * ----------
     */
    private final static String rm_months_upper[] = {"XII", "XI", "X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I"};
    private final static String rm_months_lower[] = {"xii", "xi", "x", "ix", "viii", "vii", "vi", "v", "iv", "iii", "ii", "i"};

    /* ----------
     * Roman numbers
     * ----------
     */
    private final static String rm1[] = {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
    private final static String rm10[] = {"X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private final static String rm100[] = {"C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};

    /* ----------
     * Ordinal postfixes
     * ----------
     */
    private final static String numTH[] = {"ST", "ND", "RD", "TH"};
    private final static String numth[] = {"st", "nd", "rd", "th"};

    /* ----------
     * Flags & Options:
     * ----------
     */
    public final static int ONE_UPPER = 1;		//* Name */
    public final static int ALL_UPPER = 2;		//* NAME */
    public final static int ALL_LOWER = 3;		//* name */
    //
    public final static int FULL_SIZ = 0;
    //
    public final static int MAX_MONTH_LEN = 9;
    public final static int MAX_MON_LEN = 3;
    public final static int MAX_DAY_LEN = 9;
    public final static int MAX_DY_LEN = 3;
    public final static int MAX_RM_LEN = 4;
    //
    public final static int TH_UPPER = 1;
    public final static int TH_LOWER = 2;
    //
    public final static int FROM_CHAR_DATE_NONE = 0; //* Value does not affect date mode. */
    public final static int FROM_CHAR_DATE_GREGORIAN = 1; //* Gregorian (day, month, year) style date */
    public final static int FROM_CHAR_DATE_ISOWEEK = 2;		//* ISO 8601 week date */
    /*****************************************************************************
     *			KeyWord definitions
     *****************************************************************************/

    /* ----------
     * Suffixes:
     * ----------
     */
    private final static int DCH_S_FM = 0x01;
    private final static int DCH_S_TH = 0x02;
    private final static int DCH_S_th = 0x04;
    private final static int DCH_S_SP = 0x08;
    private final static int DCH_S_TM = 0x10;

    /* ----------
     * Suffix tests
     * ----------
     */
    private boolean S_THth(int _s) {
        return ((_s & DCH_S_TH) | (_s & DCH_S_th)) != 0;
    }

    private boolean S_TH(int _s) {
        return (_s & DCH_S_TH) != 0;
    }

    private boolean S_th(int _s) {
        return ((_s) & DCH_S_th) != 0;
    }

    private int S_TH_TYPE(int _s) {
        return ((_s) & DCH_S_TH) != 0 ? TH_UPPER : TH_LOWER;
    }

    /* Oracle toggles FM behavior, we don't; see docs. */
    private boolean S_FM(int _s) {
        return ((_s) & DCH_S_FM) != 0;
    }

    private boolean S_SP(int _s) {
        return ((_s) & DCH_S_SP) != 0;
    }

    private boolean S_TM(int _s) {
        return ((_s) & DCH_S_TM) != 0;
    }

    private int SKIP_THth(int _suf) {
        return S_THth(_suf) ? 2 : 0;
    }

    /* ----------
     * Suffixes definition for DATE-TIME TO/FROM CHAR
     * ----------
     */
    private final KeySuffix DCH_suff[] = {
        new KeySuffix("FM", 2, DCH_S_FM, SUFFTYPE_PREFIX),
        new KeySuffix("fm", 2, DCH_S_FM, SUFFTYPE_PREFIX),
        new KeySuffix("TM", 2, DCH_S_TM, SUFFTYPE_PREFIX),
        new KeySuffix("tm", 2, DCH_S_TM, SUFFTYPE_PREFIX),
        new KeySuffix("TH", 2, DCH_S_TH, SUFFTYPE_POSTFIX),
        new KeySuffix("th", 2, DCH_S_th, SUFFTYPE_POSTFIX),
        new KeySuffix("SP", 2, DCH_S_SP, SUFFTYPE_POSTFIX)};

    /* ----------
     * Format-pictures (KeyWord).
     *
     * The KeyWord field; alphabetic sorted, *BUT* strings alike is sorted
     *		  complicated -to-> easy:
     *
     *	(example: "DDD","DD","Day","D" )
     *
     * (this specific sort needs the algorithm for sequential search for strings,
     * which not has exact end; -> How keyword is in "HH12blabla" ? - "HH"
     * or "HH12"? You must first try "HH12", because "HH" is in string, but
     * it is not good.
     *
     * (!)
     *	 - Position for the keyword is similar as position in the enum DCH/NUM_poz.
     * (!)
     *
     * For fast search is used the 'int index[]', index is ascii table from position
     * 32 (' ') to 126 (~), in this index is DCH_ / NUM_ enums for each ASCII
     * position or -1 if char is not used in the KeyWord. Search example for
     * string "MM":
     *	1)	see in index to index['M' - 32],
     *	2)	take keywords position (enum DCH_MI) from index
     *	3)	run sequential search in keywords[] from this position
     *
     * ----------
     */
    private final static int DCH_A_D = 0,
            DCH_A_M = 1,
            DCH_AD = 2,
            DCH_AM = 3,
            DCH_B_C = 4,
            DCH_BC = 5,
            DCH_CC = 6,
            DCH_DAY = 7,
            DCH_DDD = 8,
            DCH_DD = 9,
            DCH_DY = 10,
            DCH_Day = 11,
            DCH_Dy = 12,
            DCH_D = 13,
            DCH_FX = 14,
            DCH_HH24 = 15,
            DCH_HH12 = 16,
            DCH_HH = 17,
            DCH_IDDD = 18,
            DCH_ID = 19,
            DCH_IW = 20,
            DCH_IYYY = 21,
            DCH_IYY = 22,
            DCH_IY = 23,
            DCH_I = 24,
            DCH_J = 25,
            DCH_MI = 26,
            DCH_MM = 27,
            DCH_MONTH = 28,
            DCH_MON = 29,
            DCH_MS = 30,
            DCH_Month = 31,
            DCH_Mon = 32,
            DCH_NS = 33,
            DCH_P_M = 34,
            DCH_PM = 35,
            DCH_Q = 36,
            DCH_RM = 37,
            DCH_SSSS = 38,
            DCH_SS = 39,
            DCH_TZ = 40,
            DCH_US = 41,
            DCH_WW = 42,
            DCH_W = 43,
            DCH_Y_YYY = 44,
            DCH_YYYY = 45,
            DCH_YYY = 46,
            DCH_YY = 47,
            DCH_Y = 48,
            DCH_a_d = 49,
            DCH_a_m = 50,
            DCH_ad = 51,
            DCH_am = 52,
            DCH_b_c = 53,
            DCH_bc = 54,
            DCH_cc = 55,
            DCH_day = 56,
            DCH_ddd = 57,
            DCH_dd = 58,
            DCH_dy = 59,
            DCH_d = 60,
            DCH_fx = 61,
            DCH_hh24 = 62,
            DCH_hh12 = 63,
            DCH_hh = 64,
            DCH_iddd = 65,
            DCH_id = 66,
            DCH_iw = 67,
            DCH_iyyy = 68,
            DCH_iyy = 69,
            DCH_iy = 70,
            DCH_i = 71,
            DCH_j = 72,
            DCH_mi = 73,
            DCH_mm = 74,
            DCH_month = 75,
            DCH_mon = 76,
            DCH_ms = 77,
            DCH_ns = 78,
            DCH_p_m = 79,
            DCH_pm = 80,
            DCH_q = 81,
            DCH_rm = 82,
            DCH_ssss = 83,
            DCH_ss = 84,
            DCH_tz = 85,
            DCH_us = 86,
            DCH_ww = 87,
            DCH_w = 88,
            DCH_y_yyy = 89,
            DCH_yyyy = 90,
            DCH_yyy = 91,
            DCH_yy = 92,
            DCH_y = 93;
    /* ----------
     * KeyWords for DATE-TIME version
     * ----------
     */
    private final static KeyWord DCH_keywords[] = {
        /*          name,  len,   id,   is_digit,   date_mode */
        new KeyWord("A.D.", 4, DCH_A_D, false, FROM_CHAR_DATE_NONE), /* A */
        new KeyWord("A.M.", 4, DCH_A_M, false, FROM_CHAR_DATE_NONE),
        new KeyWord("AD", 2, DCH_AD, false, FROM_CHAR_DATE_NONE),
        new KeyWord("AM", 2, DCH_AM, false, FROM_CHAR_DATE_NONE),
        new KeyWord("B.C.", 4, DCH_B_C, false, FROM_CHAR_DATE_NONE), /* B */
        new KeyWord("BC", 2, DCH_BC, false, FROM_CHAR_DATE_NONE),
        new KeyWord("CC", 2, DCH_CC, true, FROM_CHAR_DATE_NONE), /* C */
        new KeyWord("DAY", 3, DCH_DAY, false, FROM_CHAR_DATE_NONE), /* D */
        new KeyWord("DDD", 3, DCH_DDD, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("DD", 2, DCH_DD, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("DY", 2, DCH_DY, false, FROM_CHAR_DATE_NONE),
        new KeyWord("Day", 3, DCH_Day, false, FROM_CHAR_DATE_NONE),
        new KeyWord("Dy", 2, DCH_Dy, false, FROM_CHAR_DATE_NONE),
        new KeyWord("D", 1, DCH_D, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("FX", 2, DCH_FX, false, FROM_CHAR_DATE_NONE), /* F */
        new KeyWord("HH24", 4, DCH_HH24, true, FROM_CHAR_DATE_NONE), /* H */
        new KeyWord("HH12", 4, DCH_HH12, true, FROM_CHAR_DATE_NONE),
        new KeyWord("HH", 2, DCH_HH, true, FROM_CHAR_DATE_NONE),
        new KeyWord("IDDD", 4, DCH_IDDD, true, FROM_CHAR_DATE_ISOWEEK), /* I */
        new KeyWord("ID", 2, DCH_ID, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("IW", 2, DCH_IW, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("IYYY", 4, DCH_IYYY, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("IYY", 3, DCH_IYY, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("IY", 2, DCH_IY, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("I", 1, DCH_I, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("J", 1, DCH_J, true, FROM_CHAR_DATE_NONE), /* J */
        new KeyWord("MI", 2, DCH_MI, true, FROM_CHAR_DATE_NONE), /* M */
        new KeyWord("MM", 2, DCH_MM, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("MONTH", 5, DCH_MONTH, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("MON", 3, DCH_MON, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("MS", 2, DCH_MS, true, FROM_CHAR_DATE_NONE),
        new KeyWord("Month", 5, DCH_Month, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("Mon", 3, DCH_Mon, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("NS", 2, DCH_NS, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("P.M.", 4, DCH_P_M, false, FROM_CHAR_DATE_NONE), /* P */
        new KeyWord("PM", 2, DCH_PM, false, FROM_CHAR_DATE_NONE),
        new KeyWord("Q", 1, DCH_Q, true, FROM_CHAR_DATE_NONE), /* Q */
        new KeyWord("RM", 2, DCH_RM, false, FROM_CHAR_DATE_GREGORIAN), /* R */
        new KeyWord("SSSS", 4, DCH_SSSS, true, FROM_CHAR_DATE_NONE), /* S */
        new KeyWord("SS", 2, DCH_SS, true, FROM_CHAR_DATE_NONE),
        new KeyWord("TZ", 2, DCH_TZ, false, FROM_CHAR_DATE_NONE), /* T */
        new KeyWord("US", 2, DCH_US, true, FROM_CHAR_DATE_NONE), /* U */
        new KeyWord("WW", 2, DCH_WW, true, FROM_CHAR_DATE_GREGORIAN), /* W */
        new KeyWord("W", 1, DCH_W, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("Y,YYY", 5, DCH_Y_YYY, true, FROM_CHAR_DATE_GREGORIAN), /* Y */
        new KeyWord("YYYY", 4, DCH_YYYY, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("YYY", 3, DCH_YYY, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("YY", 2, DCH_YY, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("Y", 1, DCH_Y, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("a.d.", 4, DCH_a_d, false, FROM_CHAR_DATE_NONE), /* a */
        new KeyWord("a.m.", 4, DCH_a_m, false, FROM_CHAR_DATE_NONE),
        new KeyWord("ad", 2, DCH_ad, false, FROM_CHAR_DATE_NONE),
        new KeyWord("am", 2, DCH_am, false, FROM_CHAR_DATE_NONE),
        new KeyWord("b.c.", 4, DCH_b_c, false, FROM_CHAR_DATE_NONE), /* b */
        new KeyWord("bc", 2, DCH_bc, false, FROM_CHAR_DATE_NONE),
        new KeyWord("cc", 2, DCH_CC, true, FROM_CHAR_DATE_NONE), /* c */
        new KeyWord("day", 3, DCH_day, false, FROM_CHAR_DATE_NONE), /* d */
        new KeyWord("ddd", 3, DCH_DDD, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("dd", 2, DCH_DD, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("dy", 2, DCH_dy, false, FROM_CHAR_DATE_NONE),
        new KeyWord("d", 1, DCH_D, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("fx", 2, DCH_FX, false, FROM_CHAR_DATE_NONE), /* f */
        new KeyWord("hh24", 4, DCH_HH24, true, FROM_CHAR_DATE_NONE), /* h */
        new KeyWord("hh12", 4, DCH_HH12, true, FROM_CHAR_DATE_NONE),
        new KeyWord("hh", 2, DCH_HH, true, FROM_CHAR_DATE_NONE),
        new KeyWord("iddd", 4, DCH_IDDD, true, FROM_CHAR_DATE_ISOWEEK), /* i */
        new KeyWord("id", 2, DCH_ID, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("iw", 2, DCH_IW, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("iyyy", 4, DCH_IYYY, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("iyy", 3, DCH_IYY, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("iy", 2, DCH_IY, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("i", 1, DCH_I, true, FROM_CHAR_DATE_ISOWEEK),
        new KeyWord("j", 1, DCH_J, true, FROM_CHAR_DATE_NONE), /* j */
        new KeyWord("mi", 2, DCH_MI, true, FROM_CHAR_DATE_NONE), /* m */
        new KeyWord("mm", 2, DCH_MM, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("month", 5, DCH_month, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("mon", 3, DCH_mon, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("ms", 2, DCH_MS, true, FROM_CHAR_DATE_NONE),
        new KeyWord("ns", 2, DCH_NS, false, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("p.m.", 4, DCH_p_m, false, FROM_CHAR_DATE_NONE), /* p */
        new KeyWord("pm", 2, DCH_pm, false, FROM_CHAR_DATE_NONE),
        new KeyWord("q", 1, DCH_Q, true, FROM_CHAR_DATE_NONE), /* q */
        new KeyWord("rm", 2, DCH_rm, false, FROM_CHAR_DATE_GREGORIAN), /* r */
        new KeyWord("ssss", 4, DCH_SSSS, true, FROM_CHAR_DATE_NONE), /* s */
        new KeyWord("ss", 2, DCH_SS, true, FROM_CHAR_DATE_NONE),
        new KeyWord("tz", 2, DCH_tz, false, FROM_CHAR_DATE_NONE), /* t */
        new KeyWord("us", 2, DCH_US, true, FROM_CHAR_DATE_NONE), /* u */
        new KeyWord("ww", 2, DCH_WW, true, FROM_CHAR_DATE_GREGORIAN), /* w */
        new KeyWord("w", 1, DCH_W, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("y,yyy", 5, DCH_Y_YYY, true, FROM_CHAR_DATE_GREGORIAN), /* y */
        new KeyWord("yyyy", 4, DCH_YYYY, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("yyy", 3, DCH_YYY, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("yy", 2, DCH_YY, true, FROM_CHAR_DATE_GREGORIAN),
        new KeyWord("y", 1, DCH_Y, true, FROM_CHAR_DATE_GREGORIAN)
    };
    /* ----------
     * KeyWords index for DATE-TIME version
     * ----------
     */
    private final static int DCH_index[] = {
        /*
        0	1	2	3	4	5	6	7	8	9
         */
        //*---- first 0..31 chars are skipped ----*/
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, DCH_A_D, DCH_B_C, DCH_CC, DCH_DAY, -1,
        DCH_FX, -1, DCH_HH24, DCH_IDDD, DCH_J, -1, -1, DCH_MI, DCH_NS, -1,
        DCH_P_M, DCH_Q, DCH_RM, DCH_SSSS, DCH_TZ, DCH_US, -1, DCH_WW, -1, DCH_Y_YYY,
        -1, -1, -1, -1, -1, -1, -1, DCH_a_d, DCH_b_c, DCH_cc,
        DCH_day, -1, DCH_fx, -1, DCH_hh24, DCH_iddd, DCH_j, -1, -1, DCH_mi,
        DCH_ns, -1, DCH_p_m, DCH_q, DCH_rm, DCH_ssss, DCH_tz, DCH_us, -1, DCH_ww,
        -1, DCH_y_yyy, -1, -1, -1, -1
    //*---- chars over 126 are skipped ----*/
    };
    //

    private void from_char_set_mode(TmFromChar tmfc, int mode) {
        if (mode != FROM_CHAR_DATE_NONE) {
            if (tmfc.mode == FROM_CHAR_DATE_NONE) {
                tmfc.mode = mode;
            } else if (tmfc.mode != mode) {
                System.out.println("invalid combination of date conventions.\nDo not mix Gregorian and ISO week date conventions in a formatting template.");
            }
        }
    }

    //
    //Pair<value,used>
    private Pair from_char_parse_int_len(int dest, char src[], int start, int len, FormatNode node, FormatNode nextnode) {
        long result;
        int used;
        Pair pair = new Pair();
        /*
         * Skip any whitespace before parsing the integer.
         */
        assert (len <= DCH_MAX_ITEM_SIZ);
        int space = strspace_len(src, start);
        int digit = trdigits_len(src, start);
        digit -= space;
        used = Math.min(src.length - start, len);
        used = Math.min(used, digit);
        //start + space, start + space + used + 1 为数字
        String copy = String.valueOf(src).substring(start + space, start + space + used);

        if (S_FM(node.suffix) || is_next_separator(node, nextnode)) {
            /*
             * This node is in Fill Mode, or the next node is known to be a
             * non-digit value, so we just slurp as many characters as we can get.
             */
            result = Integer.parseInt(copy); //bug
        } else {
            /*
             * We need to pull exactly the number of characters given in 'len' out
             * of the string, and convert those.
             */
            System.out.println("");
            if (used < len) {
                System.out.println("source string too short for \"" + node.key.name + "\" formatting field.\n");
                System.out.println("Field requires " + len + " characters, but only " + used + " remain.\n");
                System.out.println("If your source string is not fixed-width, try using the \"FM\" modifier.");
            }

            result = Integer.parseInt(copy);

            if (used > 0 && used < len) {
                System.out.println("invalid value \"" + copy + "\" for \"" + node.key.name + "\"");
                System.out.println("Field requires " + len + " characters, but only " + used + " could be parsed.");
                System.out.println("If your source string is not fixed-width, try using the \"FM\" modifier.");
            }
        }

        if (used == 0) {
            System.out.println("invalid value \"" + copy + "\" for \"" + node.key.name + "\"");
            System.out.println("Value must be an integer.");
        }

        if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE) {
            System.out.println("value for \"" + node.key.name + "\" in source string is out of range");
            System.out.println("Value must be in the range " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE + ".");
        }

        pair.first = from_char_set_int(dest, (int) result, node);
        pair.second = used + space;
        return pair;
    }

    private Pair from_char_parse_int(int dest, char src[], int start, FormatNode node, FormatNode nextnode) {
        return from_char_parse_int_len(dest, src, start, node.key.len, node, nextnode);
    }

    private TmFromChar DCH_from_char(FormatNode[] format, String date_str) {
        boolean fx_mode = false;
        Pair pair;
        TmFromChar tmfc = new TmFromChar();
        FormatNode n = format[0];
        char cs[] = date_str.toCharArray();
        for (int np = 0, start = 0; n.type != NODE_TYPE_END && start < cs.length; n = format[++np]) {
            if (n.type != NODE_TYPE_ACTION) {
                start++;

                /* Ignore spaces when not in FX (fixed width) mode */
                if (Character.isSpaceChar(n.character) && !fx_mode) {
                    while ((start < cs.length) && Character.isSpaceChar(cs[start])) {
                        start++;
                    }
                }
                continue;
            }
            from_char_set_mode(tmfc, n.key.date_mode);

            switch (n.key.id) {
                case DCH_FX:
                    fx_mode = true;
                    break;
                /* http://space.itpub.net/519536/viewspace-617289 */
                case DCH_A_M:
                case DCH_P_M:
                case DCH_a_m:
                case DCH_p_m:
                    pair = from_char_seq_search(cs, start, ampm_strings_long, ALL_UPPER, n.key.len, n);
                    tmfc.pm = from_char_set_int(tmfc.pm, pair.first % 2, n);
                    tmfc.clock = CLOCK_12_HOUR;
                    start += pair.second;
                    break;
                case DCH_AM:
                case DCH_PM:
                case DCH_am:
                case DCH_pm:
                    pair = from_char_seq_search(cs, start, ampm_strings, ALL_UPPER, n.key.len, n);
                    tmfc.pm = from_char_set_int(tmfc.pm, pair.first % 2, n);
                    tmfc.clock = CLOCK_12_HOUR;
                    start += pair.second;
                    break;
                case DCH_HH:
                case DCH_HH12:
                    pair = from_char_parse_int_len(tmfc.hh, cs, start, 2, n, format[np + 1]);
                    tmfc.clock = CLOCK_12_HOUR;
                    tmfc.hh = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;
                case DCH_HH24:
                    pair = from_char_parse_int_len(tmfc.hh, cs, start, 2, n, format[np + 1]);
                    tmfc.clock = CLOCK_24_HOUR;
                    tmfc.hh = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_MI:
                    pair = from_char_parse_int(tmfc.mi, cs, start, n, format[np + 1]);
                    tmfc.mi = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_SS:
                    pair = from_char_parse_int(tmfc.ss, cs, start, n, format[np + 1]);
                    tmfc.ss = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_MS:		/* millisecond */
                    pair = from_char_parse_int_len(tmfc.ms, cs, start, 3, n, format[np + 1]);
                    tmfc.ms = pair.first;
                    start += pair.second;
                    /*
                     * 25 is 0.25 and 250 is 0.25 too; 025 is 0.025 and not 0.25
                     */
                    tmfc.ms *= pair.second == 1 ? 100 : pair.second == 2 ? 10 : 1;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_US:		/* microsecond */
                    pair = from_char_parse_int_len(tmfc.us, cs, start, 6, n, format[np + 1]);
                    tmfc.us = pair.first;
                    start += pair.second;
                    /*
                     * 25 is 0.25 and 250 is 0.25 too; 025 is 0.025 and not 0.25
                     */
                    tmfc.us *= pair.second == 1 ? 100000
                            : pair.second == 2 ? 10000
                            : pair.second == 3 ? 1000
                            : pair.second == 4 ? 100
                            : pair.second == 5 ? 10 : 1;
                    start += SKIP_THth(n.suffix);
                    break;
                case DCH_NS:		/* nanosecond */
                    pair = from_char_parse_int_len(tmfc.ns, cs, start, 9, n, format[np + 1]);
                    tmfc.ns = pair.first;
                    start += pair.second;
                    /*
                     * 25 is 0.25 and 250 is 0.25 too; 025 is 0.025 and not 0.25
                     */
                    tmfc.ns *= pair.second == 1 ? 100000000
                            : pair.second == 2 ? 10000000
                            : pair.second == 3 ? 1000000
                            : pair.second == 4 ? 100000
                            : pair.second == 5 ? 10000
                            : pair.second == 6 ? 1000
                            : pair.second == 7 ? 100
                            : pair.second == 8 ? 10
                            : 1;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_SSSS:
                    pair = from_char_parse_int(tmfc.ssss, cs, start, n, format[np + 1]);
                    tmfc.ssss = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_tz:
                case DCH_TZ:
                    System.out.println("\"TZ\"/\"tz\" format patterns are not supported in to_date");
                    break;

                case DCH_A_D:
                case DCH_B_C:
                case DCH_a_d:
                case DCH_b_c:
                    pair = from_char_seq_search(cs, start, adbc_strings_long, ALL_UPPER, n.key.len, n);
                    tmfc.bc = from_char_set_int(tmfc.bc, pair.first % 2, n);
                    start += pair.second;
                    break;

                case DCH_AD:
                case DCH_BC:
                case DCH_ad:
                case DCH_bc:
                    pair = from_char_seq_search(cs, start, adbc_strings, ALL_UPPER, n.key.len, n);
                    tmfc.bc = from_char_set_int(tmfc.bc, pair.first % 2, n);
                    start += pair.second;
                    break;

                case DCH_MONTH:
                case DCH_Month:
                case DCH_month:
                    pair = from_char_seq_search(cs, start, months_full, ONE_UPPER, MAX_MONTH_LEN, n);
                    tmfc.mm = from_char_set_int(tmfc.mm, pair.first + 1, n);
                    start += pair.second;
                    break;

                case DCH_MON:
                case DCH_Mon:
                case DCH_mon:
                    pair = from_char_seq_search(cs, start, months, ONE_UPPER, MAX_MONTH_LEN, n);
                    tmfc.mm = from_char_set_int(tmfc.mm, pair.first + 1, n);
                    start += pair.second;
                    break;

                case DCH_MM:
                    pair = from_char_parse_int(tmfc.mm, cs, start, n, format[np + 1]);
                    tmfc.mm = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_DAY:
                case DCH_Day:
                case DCH_day:
                    pair = from_char_seq_search(cs, start, days, ONE_UPPER, MAX_DAY_LEN, n);
                    tmfc.d = from_char_set_int(tmfc.d, pair.first, n);
                    start += pair.second;
                    break;

                case DCH_DY:
                case DCH_Dy:
                case DCH_dy:
                    pair = from_char_seq_search(cs, start, days, ONE_UPPER, MAX_DY_LEN, n);
                    tmfc.d = from_char_set_int(tmfc.d, pair.first, n);
                    start += pair.second;
                    break;

                case DCH_DDD:
                    pair = from_char_parse_int(tmfc.ddd, cs, start, n, format[np + 1]);
                    tmfc.ddd = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_IDDD:
                    pair = from_char_parse_int_len(tmfc.ddd, cs, start, 3, n, format[np + 1]);
                    tmfc.ddd = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_DD:
                    pair = from_char_parse_int(tmfc.dd, cs, start, n, format[np + 1]);
                    tmfc.dd = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_D:
                    pair = from_char_parse_int(tmfc.d, cs, start, n, format[np + 1]);
                    tmfc.d = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_ID:
                    pair = from_char_parse_int_len(tmfc.d, cs, start, 1, n, format[np + 1]);
                    tmfc.d = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_WW:
                case DCH_IW:
                    pair = from_char_parse_int(tmfc.ww, cs, start, n, format[np + 1]);
                    tmfc.ww = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_Q:
                    /*
                     * We ignore 'Q' when converting to date because it is unclear
                     * which date in the quarter to use, and some people specify
                     * both quarter and month, so if it was honored it might
                     * conflict with the supplied month. That is also why we don't
                     * throw an error.
                     *
                     * We still parse the source string for an integer, but it
                     * isn't stored anywhere in 'out'.
                     */
                    pair = from_char_parse_int(tmfc.q, cs, start, n, format[np + 1]);
                    tmfc.q = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_CC:
                    pair = from_char_parse_int(tmfc.cc, cs, start, n, format[np + 1]);
                    tmfc.cc = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_Y_YYY: {
                    int matched = 0, years = 0, millenia = 0;
                    int k = start;
                    for (; k < cs.length; k++) {
                        if (cs[k] != ',' && Character.isDigit(cs[k])) {
                            millenia = millenia * 10 + cs[k] - '0';
                        } else {
                            break;
                        }
                    }
                    if (millenia != 0) {
                        matched++;
                        if (k < cs.length && cs[k] == ',') {
                            int m = 3;
                            for (k++; k < cs.length && (m-- > 0); k++) {
                                if (Character.isDigit(cs[k])) {
                                    years = years * 10 + cs[k] - '0';
                                } else {
                                    break;
                                }
                            }
                            if (m < 3) {
                                matched++;
                            }
                        }
                    }

                    if (matched != 2) {
                        System.out.println("invalid input string for \"Y,YYY\"");
                        break;
                    }

                    years += (millenia * 1000);
                    tmfc.year = from_char_set_int(tmfc.year, years, n);
                    tmfc.yysz = 4;
                    start = k;
                    start += SKIP_THth(n.suffix);
                }
                break;

                case DCH_YYYY:
                case DCH_IYYY:
                    pair = from_char_parse_int(tmfc.year, cs, start, n, format[np + 1]);
                    tmfc.yysz = 4;
                    tmfc.year = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_YYY:
                case DCH_IYY:
                    pair = from_char_parse_int(tmfc.year, cs, start, n, format[np + 1]);
                    tmfc.yysz = 3;
                    tmfc.year = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);

                    /*
                     * 3-digit year: '100' ... '999' = 1100 ... 1999 '000' ...
                     * '099' = 2000 ... 2099
                     */
                    if (tmfc.year >= 100) {
                        tmfc.year += 1000;
                    } else {
                        tmfc.year += 2000;
                    }
                    break;

                case DCH_YY:
                case DCH_IY:
                    pair = from_char_parse_int(tmfc.year, cs, start, n, format[np + 1]);
                    tmfc.yysz = 2;
                    tmfc.year = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);

                    /*
                     * 2-digit year: '00' ... '69'	= 2000 ... 2069 '70' ... '99'
                     * = 1970 ... 1999
                     */
                    if (tmfc.year < 70) {
                        tmfc.year += 2000;
                    } else {
                        tmfc.year += 1900;
                    }
                    break;

                case DCH_Y:
                case DCH_I:
                    pair = from_char_parse_int(tmfc.year, cs, start, n, format[np + 1]);
                    tmfc.yysz = 1;
                    tmfc.year = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    /*
                     * 1-digit year: always +2000
                     */
                    tmfc.year += 2000;
                    break;

                case DCH_RM:
                    pair = from_char_seq_search(cs, start, rm_months_upper, ALL_UPPER, MAX_RM_LEN, n);
                    tmfc.mm = from_char_set_int(tmfc.mm, 12 - pair.first, n);
                    start += pair.second;
                    break;

                case DCH_rm:
                    pair = from_char_seq_search(cs, start, rm_months_lower, ALL_LOWER, MAX_RM_LEN, n);
                    tmfc.mm = from_char_set_int(tmfc.mm, 12 - pair.first, n);
                    start += pair.second;
                    break;

                case DCH_W:
                    pair = from_char_parse_int(tmfc.w, cs, start, n, format[np + 1]);
                    tmfc.w = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;

                case DCH_J:
                    pair = from_char_parse_int(tmfc.j, cs, start, n, format[np + 1]);
                    tmfc.j = pair.first;
                    start += pair.second;
                    start += SKIP_THth(n.suffix);
                    break;
            }
        }
        return tmfc;
    }

    private final static class KeySuffix {

        public String name;	//* suffix string		*/
        public int len;         //* suffix length		*/
        public int id;          //* used in node->suffix        */
        public int type;        //* prefix / postfix            */

        public KeySuffix(String name, int len, int id, int type) {
            this.name = name;
            this.len = len;
            this.id = id;
            this.type = type;
        }

        @Override
        public String toString() {
            return name;
        }
    };

    private final static class KeyWord {

        public String name;
        public int len;
        public int id;
        public boolean is_digit;
        public int date_mode;

        public KeyWord(String name, int len, int id, boolean is_digit, int date_mode) {
            this.name = name;
            this.len = len;
            this.id = id;
            this.is_digit = is_digit;
            this.date_mode = date_mode;
        }

        @Override
        public String toString() {
            return name;
        }
    };

    private final static class FormatNode {

        public int type;			//* node type			*/
        public KeyWord key;			//* if node type is KEYWORD	*/
        public char character;                  //* if node type is CHAR	*/
        public int suffix;			//* keyword suffix		*/

        public FormatNode() {
            type = 0;
            key = null;
            character = '\0';
            suffix = 0;
        }

        @Override
        public String toString() {
            return key.toString();
        }
    };

    /* ----------
     * Number description struct
     * ----------
     */
    private final static class NUMDesc {

        public int pre, //* (count) numbers before decimal */
                post, //* (count) numbers after decimal  */
                lsign, //* want locales sign		  */
                flag, //* number parameters		  */
                pre_lsign_num, //* tmp value for lsign		  */
                multi, //* multiplier for 'V'		  */
                zero_start, //* position of first zero	  */
                zero_end, //* position of last zero	  */
                need_locale;	//* needs it locale		  */
    };
    /* 
     * Format picture cache
     *	cache size = DCH_CACHE_SIZE * DCH_CACHE_FIELDS
     */

    private final static class DCHCacheEntry {

        public FormatNode format[];
        public char str[];
        public int age;
    };
    private final static int DCH_CACHE_SIZE = 128;
    private final static int DCH_CACHE_FIELDS = 16;
    private static int n_DCHCache = 0;		//* number of entries */
    private static int DCHCounter = 0;
    private static DCHCacheEntry DCHCache[];

    /* ----------
     * For char->date/time conversion
     * ----------
     */
    private final static class TmFromChar {

        public int mode;
        public int hh,
                pm,
                mi,
                ss,
                ssss,
                d,
                dd,
                ddd,
                mm,
                ms,
                us,
                ns,
                year,
                bc,
                ww,
                w,
                cc,
                j,
                q,
                yysz, //* is it YY or YYYY ? */
                clock;//* 12 or 24 hour clock? */
    };

    /* ----------
     * Datetime to char conversion
     * ----------
     */
    public static class Tm {

        public int tm_nanos;
        public int tm_sec;
        public int tm_min;
        public int tm_hour;
        public int tm_mday;
        public int tm_mon;              //* origin 0, not 1 */
        public int tm_year;		//* relative to 1900 */
        public int tm_wday;
        public int tm_liangay;
        public int tm_isdst;
        public long tm_gmtoff;
        public String tm_zone;

        @Override
        public String toString() {
            return "Tm{" + "\n tm_nanos=" + tm_nanos + "\n tm_sec=" + tm_sec + "\n tm_min=" + tm_min + "\n tm_hour=" + tm_hour + "\n tm_mday=" + tm_mday + "\n tm_mon=" + tm_mon + "\n tm_year=" + tm_year + "\n tm_wday=" + tm_wday + "\n tm_liangay=" + tm_liangay + "\n tm_isdst=" + tm_isdst + "\n tm_gmtoff=" + tm_gmtoff + "\n tm_zone=" + tm_zone + "\n}";
        }
    };

    private void INVALID_FOR_INTERVAL(boolean is_interval) {
        if (is_interval) {
            System.out.println("invalid format specification for an interval value.\nIntervals are not tied to specific calendar dates.");
        }
    }

    private boolean KeyWord_INDEX_FILTER(char c) {
        return (c > ' ') && (c < '~');
    }

    /* ----------
     * Fast sequential search, use index for data selection which
     * go to seq. cycle (it is very fast for unwanted strings)
     * (can't be used binary search in format parsing)
     * ----------
     */
    private KeyWord index_seq_search(String str, int start, final KeyWord kw[], final int index[]) {
        int poz;

        char c = str.charAt(start);
        if (!KeyWord_INDEX_FILTER(c)) {
            return null;
        }

        if ((poz = index[ c - ' ']) > -1) {
            KeyWord k = kw[poz];
            do {
                if (str.startsWith(k.name, start)) {
                    return k;
                }
                ++poz;
                if (poz >= kw.length) {
                    return null;
                }
                k = kw[poz];
            } while (c == k.name.charAt(0));
        }

        return null;
    }

    private KeySuffix suff_search(String str, int start, KeySuffix suf[], int type) {

        for (KeySuffix s : suf) {
            if (s.type == type && str.startsWith(s.name, start)) {
                return s;
            }
        }
        return null;
    }

    /* ----------
     * Format parser, search small keywords and keyword's suffixes, and make
     * format-node tree.
     *
     * for DATE-TIME & NUMBER version
     * ----------
     */
    public void parse_format(FormatNode node[], String str, final KeyWord kw[], KeySuffix suf[], final int index[]) {
        int np = 0;
        KeySuffix s = null;
        FormatNode n = node[np];
        boolean node_set = false;
        char last = '\0';
        int length = str.length();
        int suffix = 0;

        for (int start = 0; start < length;) {
            suffix = 0;
            /*
             * Prefix
             */
            if ((s = suff_search(str, start, suf, SUFFTYPE_PREFIX)) != null) {
                suffix |= s.id;

                if (s.len > 0) {
                    start += s.len;
                }
            }

            /*
             * Keyword
             */
            if ((start < length) && (n.key = index_seq_search(str, start, kw, index)) != null) {
                n.type = NODE_TYPE_ACTION;
                n.suffix = 0;
                node_set = true;

                if (n.key.len > 0) {
                    start += n.key.len;
                }

                /*
                 * Postfix
                 */
                if ((start < length) && (s = suff_search(str, start, suf, SUFFTYPE_POSTFIX)) != null) {
                    suffix |= s.id;

                    if (s.len > 0) {
                        start += s.len;
                    }
                }
            } else if (start < length) {
                /*
                 * Special characters '\' and '"'
                 */

                char c = str.charAt(start);
                if (c == '"' && last != '\\') {
                    char x = '\0';

                    while (++start < length) {
                        c = str.charAt(start);
                        if (c == '"' && x != '\\') {
                            start++;
                            break;
                        } else if (c == '\\' && x != '\\') {
                            x = '\\';
                            continue;
                        }

                        n.type = NODE_TYPE_CHAR;
                        n.character = c;
                        n.key = null;
                        n.suffix = 0;
                        n = node[++np];
                        x = c;
                    }

                    node_set = false;
                    suffix = 0;
                    last = 0;
                } else if ((start < length) && c == '\\' && last != '\\' && str.charAt(start + 1) == '"') {
                    last = c;
                    start++;
                } else if (start < length) {
                    n.type = NODE_TYPE_CHAR;
                    n.character = c;
                    n.key = null;
                    node_set = true;
                    last = 0;
                    start++;
                }
            }

            /* end */
            if (node_set) {
                if (n.type == NODE_TYPE_ACTION) {
                    n.suffix = suffix;
                }

                n = node[++np];
                n.suffix = 0;
                node_set = false;
            }
        }

        n.type = NODE_TYPE_END;
        n.suffix = 0;
        return;
    }

    /*****************************************************************************
     *			Private utils
     *****************************************************************************/
    private boolean isdigit(char c) {
        return '0' <= c && c <= '9';
    }
    /* ----------
     * Return ST/ND/RD/TH for simple (1..9) numbers
     * type --> 0 upper, 1 lower
     * ----------
     */

    private String get_th(String num, int type) {
        int len = num.length(),
                seclast;
        char last = num.charAt(len - 1);

        if (!isdigit(last)) {
            System.out.println("\"" + num + "\" is not a number");
            return null;
        }

        /*
         * All "teens" (<x>1[0-9]) get 'TH/th', while <x>[02-9][123] still get
         * 'ST/st', 'ND/nd', 'RD/rd', respectively
         */
        if ((len > 1) && (num.charAt(len - 2) == '1')) {
            last = '\0';
        }

        switch (last) {
            case '1':
                if (type == TH_UPPER) {
                    return numTH[0];
                }
                return numth[0];
            case '2':
                if (type == TH_UPPER) {
                    return numTH[1];
                }
                return numth[1];
            case '3':
                if (type == TH_UPPER) {
                    return numTH[2];
                }
                return numth[2];

            default:
                if (type == TH_UPPER) {
                    return numTH[3];
                }
                return numth[3];
        }
    }

    /* ----------
     * Convert string-number to ordinal string-number
     * type --> 0 upper, 1 lower
     * ----------
     */
    private void str_numth(StringBuilder dest, int type) {
        dest.append(get_th(dest.toString(), type));
    }

    String initcap(final String buff, int nbytes) {
        boolean wasalnum = false;
        if (buff == null) {
            return null;
        }
        char result[] = new char[Math.min(buff.length(), nbytes)];
        int i = 0;
        for (char c : buff.toCharArray()) {
            if (wasalnum) {
                result[i++] = Character.toLowerCase(c);
            } else {
                result[i++] = Character.toUpperCase(c);
            }

            wasalnum = Character.isLetterOrDigit(c);
        }

        return String.valueOf(result);
    }

    String initcap(final String buff) {
        return initcap(buff, buff.length());
    }

    int strlcpy(char dst[], final char src[], int start, int siz) {
        int i = 0;
        int n = siz;

        /* Copy as many bytes as will fit */
        if (n != 0) {
            while (--n != 0) {
                if (i >= dst.length || (i + start) >= src.length) {
                    break;
                }
                dst[i] = src[i + start];
                i++;
            }
        }
        return i;		/* count does not include NUL */
    }

    /* ----------
     * Return TRUE if next format picture is not digit value
     * ----------
     */
    private boolean is_next_separator(FormatNode node, FormatNode nextnode) {
        if (node.type == NODE_TYPE_END) {
            return false;
        }

        if (node.type == NODE_TYPE_ACTION && S_THth(node.suffix)) {
            return true;
        }

        /* end of format string is treated like a non-digit separator */
        if (nextnode.type == NODE_TYPE_END) {
            return true;
        }

        if (nextnode.type == NODE_TYPE_ACTION) {
            if (nextnode.key.is_digit) {
                return false;
            }

            return true;
        } else if (isdigit(nextnode.character)) {
            return false;
        }

        return true;				/* some non-digit input (separator) */
    }

    private int strspace_len(char cs[], int start) {
        int len = 0;
        if (cs == null) {
            return len;
        }
        while (start < cs.length && Character.isSpaceChar(cs[start++])) {
            len++;
        }
        return len;
    }

    private int trdigits_len(char cs[], int start) {
        if (cs == null) {
            return 0;
        }
        int len = strspace_len(cs, start);
        start += len;
        while (len <= DCH_MAX_ITEM_SIZ && start < cs.length && Character.isDigit(cs[start++])) {
            len++;
        }
        return len;
    }

    private boolean isleap(int y) {
        return (((y) % 4) == 0 && (((y) % 100) != 0 || ((y) % 400) == 0));
    }

    /*
     * Calendar time to Julian date conversions.
     * Julian date is commonly used in astronomical applications,
     *	since it is numerically accurate and computationally simple.
     * The algorithms here will accurately convert between Julian day
     *	and calendar date for all non-negative Julian days
     *	(i.e. from Nov 24, -4713 on).
     *
     * These routines will be used by other date/time packages
     * - thomas 97/02/25
     *
     * Rewritten to eliminate overflow problems. This now allows the
     * routines to work correctly for all Julian day counts from
     * 0 to 2147483647	(Nov 24, -4713 to Jun 3, 5874898) assuming
     * a 32-bit integer. Longer types should also work to the limits
     * of their precision.
     */
    int date2j(int y, int m, int d) {
        int julian;
        int century;

        if (m > 2) {
            m += 1;
            y += 4800;
        } else {
            m += 13;
            y += 4799;
        }

        century = y / 100;
        julian = y * 365 - 32167;
        julian += y / 4 - century + century / 4;
        julian += 7834 * m / 256 + d;
        return julian;
    }	/* date2j() */


    void j2date(int jd, int[] ymd) {
        int julian;
        int quad;
        int extra;
        int y;
        julian = jd;
        julian += 32044;
        quad = julian / 146097;
        extra = (julian - quad * 146097) * 4 + 3;
        julian += 60 + quad * 3 + extra / 146097;
        quad = julian / 1461;
        julian -= quad * 1461;
        y = julian * 4 / 1461;
        julian = ((y != 0) ? ((julian + 305) % 365) : ((julian + 306) % 366)) + 123;
        y += quad * 4;
        ymd[0] = y - 4800;
        quad = julian * 2141 / 65536;
        ymd[2] = julian - 7834 * quad / 256;
        ymd[1] = (quad + 10) % 12 + 1;
    }	/* j2date() */

    /*
     * j2day - convert Julian date to day-of-week (0..6 == Sun..Sat)
     *
     * Note: various places use the locution j2day(date - 1) to produce a
     * result according to the convention 0..6 = Mon..Sun.	This is a bit of
     * a crock, but will work as long as the computation here is just a modulo.
     */

    int j2day(int date) {
        int day = date;
        day += 1;
        day %= 7;
        return (int) day;
    }	/* j2day() */


    int isoweek2j(int year, int week) {
        /* fourth day of current year */
        int day4 = date2j(year, 1, 4);
        /* day0 == offset to first day of week (Monday) */
        int day0 = j2day(day4 - 1);
        return ((week - 1) * 7) + (day4 - day0);
    }

    void isoweekdate2date(int isoweek, int isowday, int[] ymd) {
        int jday = isoweek2j(ymd[0], isoweek);
        jday += isowday - 1;
        j2date(jday, ymd);

    }

    int date2isoyear(int year, int mon, int mday) {
        /* current day */
        int dayn = date2j(year, mon, mday);
        /* fourth day of current year */
        int day4 = date2j(year, 1, 4);
        /* day0 == offset to first day of week (Monday) */
        int day0 = j2day(day4 - 1);

        /*
         * We need the first week containing a Thursday, otherwise this day falls
         * into the previous year for purposes of counting weeks
         */
        if (dayn < day4 - day0) {
            day4 = date2j(year - 1, 1, 4);
            /* day0 == offset to first day of week (Monday) */
            day0 = j2day(day4 - 1);
            year--;
        }

        double result = (dayn - (day4 - day0)) / 7 + 1;

        /*
         * Sometimes the last few days in a year will fall into the first week of
         * the next year, so check for this.
         */
        if (result >= 52) {
            day4 = date2j(year + 1, 1, 4);
            /* day0 == offset to first day of week (Monday) */
            day0 = j2day(day4 - 1);

            if (dayn >= day4 - day0) {
                year++;
            }
        }

        return year;
    }

    int date2isoyearday(int year, int mon, int mday) {
        return date2j(year, mon, mday) - isoweek2j(date2isoyear(year, mon, mday), 1) + 1;
    }

    /* isoweek2date()
     * Convert ISO week of year number to date.
     * The year field must be specified with the ISO year!
     * karel 2000/08/07
     */
    void isoweek2date(int woy, int ymd[]) {
        j2date(isoweek2j(ymd[0], woy), ymd);
    }

    int date2isoweek(int year, int mon, int mday) {
        /* current day */
        int dayn = date2j(year, mon, mday);
        /* fourth day of current year */
        int day4 = date2j(year, 1, 4);
        /* day0 == offset to first day of week (Monday) */
        int day0 = j2day(day4 - 1);

        /*
         * We need the first week containing a Thursday, otherwise this day falls
         * into the previous year for purposes of counting weeks
         */
        if (dayn < day4 - day0) {
            day4 = date2j(year - 1, 1, 4);
            /* day0 == offset to first day of week (Monday) */
            day0 = j2day(day4 - 1);
        }

        double result = (dayn - (day4 - day0)) / 7 + 1;

        /*
         * Sometimes the last few days in a year will fall into the first week of
         * the next year, so check for this.
         */
        if (result >= 52) {
            day4 = date2j(year + 1, 1, 4);
            /* day0 == offset to first day of week (Monday) */
            day0 = j2day(day4 - 1);

            if (dayn >= day4 - day0) {
                result = (dayn - (day4 - day0)) / 7 + 1;
            }
        }

        return (int) result;
    }

    private Tm do_to_timestamp(String date_str, String fmt_str) {
        FormatNode format[];
        TmFromChar tmfc = new TmFromChar();
        int fmt_len = fmt_str.length();
        int ymd[] = {0, 0, 0};
        if (fmt_len > 0) {
            format = new FormatNode[fmt_len + 1];
            for (int i = 0; i < fmt_len + 1; i++) {
                format[i] = new FormatNode();
            }
            parse_format(format, fmt_str, DCH_keywords, DCH_suff, DCH_index);
//            format[fmt_len].type = NODE_TYPE_END;	/* Paranoia? */
            tmfc = DCH_from_char(format, date_str);
        }

        /*
         * Convert values that user define for FROM_CHAR (to_date/to_timestamp) to
         * standard 'tm'
         */
        Tm tm = new Tm();
        if (tmfc.ssss != 0) {
            int x = tmfc.ssss;
            tm.tm_hour = x / SECS_PER_HOUR;
            x %= SECS_PER_HOUR;
            tm.tm_min = x / SECS_PER_MINUTE;
            x %= SECS_PER_MINUTE;
            tm.tm_sec = x;
        }

        if (tmfc.ss != 0) {
            tm.tm_sec = tmfc.ss;
        }

        if (tmfc.mi != 0) {
            tm.tm_min = tmfc.mi;
        }

        if (tmfc.hh != 0) {
            tm.tm_hour = tmfc.hh;
        }

        if (tmfc.clock == CLOCK_12_HOUR) {
            if (tm.tm_hour < 1 || tm.tm_hour > 12) {
                System.out.println("");
                System.out.println("hour \"" + tm.tm_hour + "\" is invalid for the 12-hour clock\n");
                System.out.println("Use the 24-hour clock, or give an hour between 1 and 12.");
                return tm;
            }

            if (tmfc.pm != 0 && tm.tm_hour < 12) {
                tm.tm_hour += 12;
            } else if (tmfc.pm == 0 && tm.tm_hour == 12) {
                tm.tm_hour = 0;
            }
        }

        if (tmfc.year != 0) {
            /*
             * If CC and YY (or Y) are provided, use YY as 2 low-order digits for
             * the year in the given century.  Keep in mind that the 21st century
             * runs from 2001-2100, not 2000-2099.
             *
             * If a 4-digit year is provided, we use that and ignore CC.
             */
            if (tmfc.cc != 0 && tmfc.yysz <= 2) {
                tm.tm_year = tmfc.year % 100;

                if (tm.tm_year != 0) {
                    tm.tm_year += (tmfc.cc - 1) * 100;
                } else {
                    tm.tm_year = tmfc.cc * 100;
                }
            } else {
                tm.tm_year = tmfc.year;
            }
        } else if (tmfc.cc != 0) /* use first year of century */ {
            tm.tm_year = (tmfc.cc - 1) * 100 + 1;
        }

        if (tmfc.bc != 0) {
            if (tm.tm_year > 0) {
                tm.tm_year = -(tm.tm_year - 1);
            } else {
                System.out.println("inconsistent use of year %04d and \"" + tm.tm_year + "\"");
                return tm;
            }
        }

        if (tmfc.j != 0) {
            j2date(tmfc.j, ymd);
            tm.tm_year = ymd[0];
            tm.tm_mon = ymd[1];
            tm.tm_mday = ymd[2];
        }

        if (tmfc.ww != 0) {
            if (tmfc.mode == FROM_CHAR_DATE_ISOWEEK) {
                /*
                 * If tmfc.d is not set, then the date is left at the beginning of
                 * the ISO week (Monday).
                 */
                if (tmfc.d != 0) {
                    isoweekdate2date(tmfc.ww, tmfc.d, ymd);
                    tm.tm_year = ymd[0];
                    tm.tm_mon = ymd[1];
                    tm.tm_mday = ymd[2];
                } else {
                    isoweek2date(tmfc.ww, ymd);
                    tm.tm_year = ymd[0];
                    tm.tm_mon = ymd[1];
                    tm.tm_mday = ymd[2];
                }
            } else {
                tmfc.ddd = (tmfc.ww - 1) * 7 + 1;
            }
        }

        if (tmfc.w != 0) {
            tmfc.dd = (tmfc.w - 1) * 7 + 1;
        }

        if (tmfc.d != 0) {
            tm.tm_wday = tmfc.d;
        }

        if (tmfc.dd != 0) {
            tm.tm_mday = tmfc.dd;
        }

        if (tmfc.ddd != 0) {
            tm.tm_liangay = tmfc.ddd;
        }

        if (tmfc.mm != 0) {
            tm.tm_mon = tmfc.mm;
        }

        if (tmfc.ddd != 0 && (tm.tm_mon <= 1 || tm.tm_mday <= 1)) {
            /*
             * The month and day field have not been set, so we use the
             * day-of-year field to populate them.	Depending on the date mode,
             * this field may be interpreted as a Gregorian day-of-year, or an ISO
             * week date day-of-year.
             */
            if (tm.tm_year == 0 && tmfc.bc == 0) {
                System.out.println("cannot calculate day of year without year information");
                return tm;
            }

            if (tmfc.mode == FROM_CHAR_DATE_ISOWEEK) {
                /* zeroth day of the ISO year, in Julian */
                int j0 = isoweek2j(tm.tm_year, 1) - 1;
                j2date(j0 + tmfc.ddd, ymd);
                tm.tm_year = ymd[0];
                tm.tm_mon = ymd[1];
                tm.tm_mday = ymd[2];
            } else {
                int[] y;
                int i;
                int ysum[][] = {
                    {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365},
                    {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366}
                };
                y = ysum[isleap(tm.tm_year) ? 1 : 0];

                for (i = 1; i <= 12; i++) {
                    if (tmfc.ddd < y[i]) {
                        break;
                    }
                }

                if (tm.tm_mon <= 1) {
                    tm.tm_mon = i;
                }

                if (tm.tm_mday <= 1) {
                    tm.tm_mday = tmfc.ddd - y[i - 1];
                }
            }
        }

        if (tmfc.ms != 0) {
            tm.tm_nanos += tmfc.ms * 1000000;
        }

        if (tmfc.us != 0) {
            tm.tm_nanos += tmfc.us * 1000;
        }

        if (tmfc.ns != 0) {
            tm.tm_nanos += tmfc.ns;
        }
        return tm;
    }

    /* ----------
     * Process a string as denoted by a list of FormatNodes.
     * The TmFromChar struct pointed to by 'out' is populated with the results.
     *
     * Note: we currently don't have any to_interval() function, so there
     * is no need here for INVALID_FOR_INTERVAL checks.
     * ----------
     */
    /* ----------
     * Sequential search with to upper/lower conversion
     * ----------
     */
    //Pair<array_index,match_name_length>
    private Pair seq_search(char name[], int start, String array[], int type, int max) {
        Pair pair = new Pair();

        if (start > name.length) {
            return pair;
        }
        int index0 = start;
        /* set first char */
        if (type == ONE_UPPER || type == ALL_UPPER) {
            name[start] = Character.toUpperCase(name[start]);
        } else if (type == ALL_LOWER) {
            name[start] = Character.toLowerCase(name[start]);
        }

        for (int last = 0, ap = 0; ap < array.length; ap++) {
            /* comperate first chars */
            if (name[index0] != array[ap].charAt(0)) {
                continue;
            }

            start = index0 + 1;
            for (int i = 1, api = 1;; api++, start++, i++) {
                /* search fragment (max) only */
                if (i == max) {
                    pair.second = i;
                    pair.first = ap;
                    return pair;
                }

                /* full size */
                if (api == array[ap].length()) {
                    pair.second = i;
                    pair.first = ap;
                    return pair;
                }

                /* Not found in array 'a' */
                if (start == name.length) {
                    break;
                }

                /*
                 * Convert (but convert new chars only)
                 */
                if (i > last) {
                    if (type == ONE_UPPER || type == ALL_LOWER) {
                        name[start] = Character.toLowerCase(name[start]);
                    } else if (type == ALL_UPPER) {
                        name[start] = Character.toUpperCase(name[start]);
                    }

                    last = i;
                }

                if (name[start] != array[ap].charAt(api)) {
                    break;
                }
            }
        }
        pair.second = -1;
        pair.first = -1;
        return pair;
    }

    /*
     * Perform a sequential search in 'array' for text matching the first 'max'
     * characters of the source string.
     *
     * If a match is found, copy the array index of the match into the integer
     * pointed to by 'dest', advance 'src' to the end of the part of the string
     * which matched, and return the number of characters consumed.
     *
     * If the string doesn't match, throw an error.
     */
    private Pair from_char_seq_search(char src[], int start, String array[], int type, int max, FormatNode node) {
        Pair pair = seq_search(src, start, array, type, max);

        if (pair.second <= 0) {
            assert (max <= DCH_MAX_ITEM_SIZ);
            String copy = String.valueOf(src).substring(start, max + 1);
            System.out.println("invalid value \"" + copy + "\" for \"" + node.key.name + "\"");
            System.out.println("The given value did not match any of the allowed values for this field.");
        }
        return pair;
    }

    private int from_char_set_int(int dest, int value, FormatNode node) {
        if (dest != 0 && dest != value) {
            System.out.println("conflicting values for \"%s\" field in formatting string.\nThis value contradicts a previous setting for the same field type.");
        }
        return value;
    }

    /*
     * Format a date/time or interval into a string according to fmt.
     * We parse fmt into a list of FormatNodes.  This is then passed to DCH_to_char
     * for formatting.
     */
    private String datetime_to_char_body(Tm tm, String fmt_str, boolean is_interval) {
        FormatNode format[];
        TmFromChar tmfc = new TmFromChar();
        int fmt_len = fmt_str.length();
        int ymd[] = {0, 0, 0};
        /*
         * Allocate new memory if format picture is bigger than static cache
         * and not use cache (call parser always)
         */
        format = new FormatNode[fmt_len + 1];
        for (int i = 0; i < fmt_len + 1; i++) {
            format[i] = new FormatNode();
        }
        parse_format(format, fmt_str, DCH_keywords, DCH_suff, DCH_index);
        /* The real work is here */
        return DCH_to_char(tm, format, is_interval);
    }

    private String DCH_to_char(Tm tm, FormatNode[] node, boolean is_interval) {
        FormatNode n = node[0];
        int i;
        /* cache localized days and months */
//    cache_locale_time();
        StringBuilder sb = new StringBuilder();

        for (int np = 0; n.type != NODE_TYPE_END; n = node[++np]) {
            if (n.type != NODE_TYPE_ACTION) {
                sb.append(n.character);
                continue;
            }

            switch (n.key.id) {
                case DCH_A_M:
                case DCH_P_M:
                    sb.append((tm.tm_hour % HOURS_PER_DAY >= HOURS_PER_DAY / 2) ? P_M_STR : A_M_STR);
                    break;

                case DCH_AM:
                case DCH_PM:
                    sb.append((tm.tm_hour % HOURS_PER_DAY >= HOURS_PER_DAY / 2) ? PM_STR : AM_STR);
                    break;

                case DCH_a_m:
                case DCH_p_m:
                    sb.append((tm.tm_hour % HOURS_PER_DAY >= HOURS_PER_DAY / 2) ? p_m_STR : a_m_STR);
                    break;

                case DCH_am:
                case DCH_pm:
                    sb.append((tm.tm_hour % HOURS_PER_DAY >= HOURS_PER_DAY / 2) ? pm_STR : am_STR);
                    break;

                case DCH_HH:
                case DCH_HH12:
                    /*
                     * display time as shown on a 12-hour clock, even for
                     * intervals
                     */
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d",
                            tm.tm_hour % (HOURS_PER_DAY / 2) == 0 ? 12 : tm.tm_hour % (HOURS_PER_DAY / 2)));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_HH24:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", tm.tm_hour));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_MI:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", tm.tm_min));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_SS:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", tm.tm_sec));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_MS:		/* millisecond */
                    sb.append(String.format("%03d", tm.tm_nanos / 1000000));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_US:		/* microsecond */
                    sb.append(String.format("%06d", tm.tm_nanos / 1000));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;
                case DCH_NS:		/* microsecond */
                    sb.append(String.format("%09d", tm.tm_nanos));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_SSSS:
                    sb.append(String.format("%d",
                            tm.tm_hour * SECS_PER_HOUR
                            + tm.tm_min * SECS_PER_MINUTE
                            + tm.tm_sec));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_tz:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_zone != null) {
                        sb.append(tm.tm_zone.toLowerCase());
                    }
                    break;

                case DCH_TZ:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_zone != null) {
                        sb.append(tm.tm_zone);
                    }
                    break;

                case DCH_A_D:
                case DCH_B_C:
                    INVALID_FOR_INTERVAL(is_interval);
                    sb.append(tm.tm_year <= 0 ? B_C_STR : A_D_STR);
                    break;

                case DCH_AD:
                case DCH_BC:
                    INVALID_FOR_INTERVAL(is_interval);
                    sb.append(tm.tm_year <= 0 ? BC_STR : AD_STR);
                    break;

                case DCH_a_d:
                case DCH_b_c:
                    INVALID_FOR_INTERVAL(is_interval);
                    sb.append(tm.tm_year <= 0 ? b_c_STR : a_d_STR);
                    break;

                case DCH_ad:
                case DCH_bc:
                    INVALID_FOR_INTERVAL(is_interval);
                    sb.append(tm.tm_year <= 0 ? bc_STR : ad_STR);
                    break;

                case DCH_MONTH:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_mon != 0) {
                        if (S_TM(n.suffix)) {
                            sb.append(localized_full_months[tm.tm_mon - 1].toUpperCase());
                        } else {
                            sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-9s", months_full[tm.tm_mon - 1].toUpperCase()));
                        }
                    }
                    break;

                case DCH_Month:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_mon != 0) {
                        if (S_TM(n.suffix)) {
                            sb.append(initcap(localized_full_months[tm.tm_mon - 1]));
                        } else {
                            sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-9s", months_full[tm.tm_mon - 1]));
                        }
                    }
                    break;

                case DCH_month:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_mon != 0) {
                        if (S_TM(n.suffix)) {
                            sb.append(localized_full_months[tm.tm_mon - 1].toLowerCase());
                        } else {
                            sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-9s", months_full_low[tm.tm_mon - 1].toLowerCase()));
                        }
                    }
                    break;

                case DCH_MON:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_mon != 0) {
                        if (S_TM(n.suffix)) {
                            sb.append(localized_abbrev_months[tm.tm_mon - 1].toUpperCase());
                        } else {
                            sb.append(months[tm.tm_mon - 1].toUpperCase());
                        }
                    }
                    break;

                case DCH_Mon:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_mon != 0) {
                        if (S_TM(n.suffix)) {
                            sb.append(initcap(localized_abbrev_months[tm.tm_mon - 1]));
                        } else {
                            sb.append(months[tm.tm_mon - 1]);
                        }
                    }
                    break;

                case DCH_mon:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (tm.tm_mon != 0) {
                        if (S_TM(n.suffix)) {
                            sb.append(localized_abbrev_months[tm.tm_mon - 1].toLowerCase());
                        } else {
                            sb.append(months_low[tm.tm_mon - 1].toLowerCase());
                        }
                    }
                    break;

                case DCH_MM:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", tm.tm_mon));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_DAY:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (S_TM(n.suffix)) {
                        sb.append(localized_full_days[tm.tm_wday].toUpperCase());
                    } else {
                        sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-9s", days[tm.tm_wday].toUpperCase()));
                    }
                    break;

                case DCH_Day:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (S_TM(n.suffix)) {
                        sb.append(initcap(localized_full_days[tm.tm_wday]));
                    } else {
                        sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-9s", days[tm.tm_wday]));
                    }
                    break;

                case DCH_day:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (S_TM(n.suffix)) {
                        sb.append(localized_full_days[tm.tm_wday].toLowerCase());
                    } else {
                        sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-9s", days_low[tm.tm_wday].toLowerCase()));
                    }
                    break;

                case DCH_DY:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (S_TM(n.suffix)) {
                        sb.append(localized_abbrev_days[tm.tm_wday].toUpperCase());
                    } else {
                        sb.append(days_short[tm.tm_wday].toUpperCase());
                    }
                    break;

                case DCH_Dy:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (S_TM(n.suffix)) {
                        sb.append(initcap(localized_abbrev_days[tm.tm_wday]));
                    } else {
                        sb.append(days_short[tm.tm_wday]);
                    }
                    break;

                case DCH_dy:
                    INVALID_FOR_INTERVAL(is_interval);

                    if (S_TM(n.suffix)) {
                        sb.append(localized_abbrev_days[tm.tm_wday].toLowerCase());
                    } else {
                        sb.append(days_short_low[tm.tm_wday]);
                    }
                    break;

                case DCH_DDD:
                case DCH_IDDD:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%03d",
                            (n.key.id == DCH_DDD)
                            ? tm.tm_liangay
                            : date2isoyearday(tm.tm_year, tm.tm_mon, tm.tm_mday)));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_DD:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", tm.tm_mday));
                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_D:
                    INVALID_FOR_INTERVAL(is_interval);
                    sb.append(String.format("%d", tm.tm_wday + 1));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_ID:
                    INVALID_FOR_INTERVAL(is_interval);
                    sb.append(String.format("%d", (tm.tm_wday == 0) ? 7 : tm.tm_wday));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_WW:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", (tm.tm_liangay - 1) / 7 + 1));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_IW:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d",
                            date2isoweek(tm.tm_year, tm.tm_mon, tm.tm_mday)));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_Q:
                    if (tm.tm_mon != 0) {
                        sb.append(String.format("%d", (tm.tm_mon - 1) / 3 + 1));
                        if (S_THth(n.suffix)) {
                            str_numth(sb, S_TH_TYPE(n.suffix));
                        }
                    }
                    break;

                case DCH_CC:
                    if (is_interval) /* straight calculation */ {
                        i = tm.tm_year / 100;
                    } else /* century 21 starts in 2001 */ {
                        i = (tm.tm_year - 1) / 100 + 1;
                    }

                    if (i <= 99 && i >= -99) {
                        sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d", i));
                    } else {
                        sb.append(String.format("%d", i));
                    }

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_Y_YYY:
                    i = ADJUST_YEAR(tm.tm_year, is_interval) / 1000;
                    sb.append(String.format("%d,%03d", i, ADJUST_YEAR(tm.tm_year, is_interval) - (i * 1000)));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_YYYY:
                case DCH_IYYY:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%04d",
                            n.key.id == DCH_YYYY
                            ? ADJUST_YEAR(tm.tm_year, is_interval)
                            : ADJUST_YEAR(date2isoyear(tm.tm_year, tm.tm_mon, tm.tm_mday), is_interval)));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_YYY:
                case DCH_IYY:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%03d",
                            (n.key.id == DCH_YYY
                            ? ADJUST_YEAR(tm.tm_year, is_interval)
                            : ADJUST_YEAR(date2isoyear(tm.tm_year, tm.tm_mon, tm.tm_mday), is_interval)) % 1000));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_YY:
                case DCH_IY:
                    sb.append(String.format(S_FM(n.suffix) ? "%d" : "%02d",
                            (n.key.id == DCH_YY
                            ? ADJUST_YEAR(tm.tm_year, is_interval)
                            : ADJUST_YEAR(date2isoyear(tm.tm_year, tm.tm_mon, tm.tm_mday), is_interval)) % 100));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_Y:
                case DCH_I:
                    sb.append(String.format("%1d",
                            (n.key.id == DCH_Y
                            ? ADJUST_YEAR(tm.tm_year, is_interval)
                            : ADJUST_YEAR(date2isoyear(tm.tm_year, tm.tm_mon, tm.tm_mday), is_interval)) % 10));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }

                    break;

                case DCH_RM:
                    if (tm.tm_mon != 0) {
                        sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-4s", rm_months_upper[12 - tm.tm_mon]));
                    }
                    break;

                case DCH_rm:
                    if (tm.tm_mon != 0) {
                        sb.append(String.format(S_FM(n.suffix) ? "%s" : "%-4s", rm_months_lower[12 - tm.tm_mon]));
                    }
                    break;

                case DCH_W:
                    sb.append(String.format("%d", (tm.tm_mday - 1) / 7 + 1));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;

                case DCH_J:
                    sb.append(String.format("%d", date2j(tm.tm_year, tm.tm_mon, tm.tm_mday)));

                    if (S_THth(n.suffix)) {
                        str_numth(sb, S_TH_TYPE(n.suffix));
                    }
                    break;
            }
        }
        return sb.toString();
    }

    public Tm to_timestamp(String date_txt, String fmt) {
        return do_to_timestamp(date_txt, fmt);
    }

    public String timestamp_to_char(Tm tm, String fmt) {

        if (fmt == null || fmt.length() <= 0 || tm == null) {
            return null;
        }

        int thisdate = date2j(tm.tm_year, tm.tm_mon, tm.tm_mday);
        tm.tm_wday = (thisdate + 1) % 7;
        tm.tm_liangay = thisdate - date2j(tm.tm_year, 1, 1) + 1;

        return datetime_to_char_body(tm, fmt, false);
    }
}
