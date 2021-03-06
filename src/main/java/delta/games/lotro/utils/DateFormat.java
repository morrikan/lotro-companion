package delta.games.lotro.utils;

import java.util.Date;

import delta.common.ui.swing.text.dates.DateCodec;

/**
 * Date format utils.
 * @author DAM
 */
public class DateFormat
{
  private static DateCodec _dateTimeCodec=new DateCodec()
  {
    @Override
    public Long parseDate(String dateStr)
    {
      return parseDateTime(dateStr,true);
    }

    @Override
    public String formatDate(Long date)
    {
      return DateFormat.formatDateTime(date);
    }
  };

  /**
   * Get the date/time codec.
   * @return the date/time codec.
   */
  public static DateCodec getDateTimeCodec()
  {
    return _dateTimeCodec;
  }

  private static String formatDateTime(Long date)
  {
    if (date!=null)
    {
      return Formats.getDateTimeString(new Date(date.longValue()));
    }
    return "";
  }

  private static Long parseDateTime(String dateStr, boolean strict)
  {
    if (dateStr==null)
    {
      return null;
    }
    if (strict)
    {
      int length=dateStr.length();
      if (length!=16) // DD/MM/YYYY HH:MM
      {
        return null;
      }
    }
    Date date=Formats.parseDate(dateStr);
    return (date!=null)?Long.valueOf(date.getTime()):null;
  }

  private static DateCodec _dateCodec=new DateCodec()
  {
    @Override
    public Long parseDate(String dateStr)
    {
      return parseDateString(dateStr,true);
    }

    @Override
    public String formatDate(Long date)
    {
      return DateFormat.formatDate(date);
    }
  };

  /**
   * Get the date codec.
   * @return the date codec.
   */
  public static DateCodec getDateCodec()
  {
    return _dateCodec;
  }

  private static String formatDate(Long date)
  {
    if (date!=null)
    {
      return Formats.getDateString(new Date(date.longValue()));
    }
    return "";
  }

  private static Long parseDateString(String dateStr, boolean strict)
  {
    if (dateStr==null)
    {
      return null;
    }
    if (strict)
    {
      int length=dateStr.length();
      if (length!=10) // DD/MM/YYYY
      {
        return null;
      }
    }
    Date date=Formats.parseDate(dateStr);
    return (date!=null)?Long.valueOf(date.getTime()):null;
  }
}
