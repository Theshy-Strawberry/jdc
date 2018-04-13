package com.senyoboss.model;

/**
 * Created by Jr.Rex on 2015/9/8.
 */
        import com.jfinal.plugin.activerecord.DbKit;
        import com.jfinal.plugin.activerecord.TableMapping;
        import net.sf.ehcache.store.disk.ods.AATreeSet;

        import java.sql.Timestamp;
        import java.util.Date;


        public abstract class Model<M extends Model> extends com.jfinal.plugin.activerecord.Model<M> {
        private static final long serialVersionUID = -992334519496260591L;
        protected static  String oracleSequenceName = null;
        protected static   String primaryKey = null;
        /**
         * Get attribute of any mysql type
         */
        public <T> T get(String attr) {
           if(DbKit.getConfig().getDialect().isOracle())
                return super.get(attr.toLowerCase());
            else
                return super.get(attr);
        }
        /**
         * Get attribute of any mysql type. Returns defaultValue if null.
         */
       public <T> T get(String attr, Object defaultValue) {
           if(DbKit.getConfig().getDialect().isOracle())
               return super.get(attr.toLowerCase(), defaultValue);
           else
               return super.get(attr);
        }

        /**
        * Get attribute of mysql type: varchar, char, enum, set, text, tinytext, mediumtext, longtext
         */
        public String getStr(String attr) {
            if(DbKit.getConfig().getDialect().isOracle())
               return super.getStr(attr.toLowerCase());
            else
               return super.getStr(attr);
       }
        /**
         * Get attribute of mysql type: int, integer, tinyint(n) n > 1, smallint, mediumint
         */
        public Integer getInt(String attr) {
           if(DbKit.getConfig().getDialect().isOracle())
               return super.getNumber(attr.toLowerCase())==null?null:super.getNumber(attr.toLowerCase()).intValue();
           else
               return super.getInt(attr);
        }
        /**
         * Get attribute of mysql type: bigint, unsign int
         */
        public Long getLong(String attr) {
            if(DbKit.getConfig().getDialect().isOracle())
               return super.getNumber(attr.toLowerCase())==null?null:super.getNumber(attr.toLowerCase()).longValue();
           else
               return super.getLong(attr);
       }
        /**
        * Get attribute of mysql type: unsigned bigint
         */
        public java.math.BigInteger getBigInteger(String attr) {
            if(DbKit.getConfig().getDialect().isOracle())
                return super.getBigInteger(attr.toLowerCase());
           else
                return getBigInteger(attr);
       }

       /**
         * Get attribute of mysql type: date, year
        */
        public java.util.Date getDate(String attr) {
          if(DbKit.getConfig().getDialect().isOracle())
                return super.getDate(attr.toLowerCase());
        else
           return super.getDate(attr);
     }

       /**
     * Get attribute of mysql type: time
     */
        public java.sql.Time getTime(String attr) {
         if(DbKit.getConfig().getDialect().isOracle())
                return super.getTime(attr.toLowerCase());
            else
             return super.getTime(attr);
       }
        /**
         * Get attribute of mysql type: timestamp, datetime
         */
       public java.sql.Timestamp getTimestamp(String attr) throws RuntimeException{
            java.sql.Timestamp re = null;
          if(DbKit.getConfig().getDialect().isOracle()){
               Object obj = super.get(attr.toLowerCase());
              if (obj == null) {
                  return re;
               }
            if(obj instanceof oracle.sql.TIMESTAMP) {
                   try {
                       re =  ((oracle.sql.TIMESTAMP) obj).timestampValue();
              } catch (Exception e) {
                       throw (new RuntimeException(e));
                }
             }
         }else {
            re =  super.getTimestamp(attr);
        }
        return re;
    }

       /**
         * Get attribute of mysql type: real, double
       */
       public Double getDouble(String attr) {
         if(DbKit.getConfig().getDialect().isOracle())
                return super.getNumber(attr.toLowerCase()).doubleValue();
           else
             return super.getDouble(attr);
     }

       /**
        * Get attribute of mysql type: float
    */
       public Float getFloat(String attr) {
         if(DbKit.getConfig().getDialect().isOracle())
               return super.getNumber(attr.toLowerCase()).floatValue();
            else
              return super.getFloat(attr);
       }
        /**
         * Get attribute of mysql type: bit, tinyint(1)
       */
       public Boolean getBoolean(String attr) {
         if(DbKit.getConfig().getDialect().isOracle())
              return super.getBoolean(attr.toLowerCase());
         else
             return super.getBoolean(attr);
       }

       /**
    * Get attribute of mysql type: decimal, numeric
    */
     public java.math.BigDecimal getBigDecimal(String attr) {
        if(DbKit.getConfig().getDialect().isOracle())
            return super.getBigDecimal(attr.toLowerCase());
         else
           return super.getBigDecimal(attr);
     }

        /**
       * Get attribute of mysql type: binary, varbinary, tinyblob, blob, mediumblob, longblob
      */
       public byte[] getBytes(String attr) {
         if(DbKit.getConfig().getDialect().isOracle())
             return super.getBytes(attr.toLowerCase());
       else
     return super.getBytes(attr);
       }

      /**
        * Get attribute of any type that extends from Number
         */
      public Number getNumber(String attr) {
         if(DbKit.getConfig().getDialect().isOracle())
               return super.getNumber(attr.toLowerCase());
       else
             return super.getNumber(attr);
      }
    public boolean save(){
        if(DbKit.getConfig().getDialect().isOracle()) {
               if (oracleSequenceName != null) {
                    if (primaryKey != null) {
                  super.set(primaryKey, oracleSequenceName + ".nextval");
                  }else{
             super.set("id", oracleSequenceName + ".nextval");
                  }
              }
       }
     return super.save();
      }
     public M set(String attr, Object value) {
           if(DbKit.getConfig().getDialect().isOracle()) {
             if (value instanceof java.util.Date) {
                 java.util.Date dt = (java.util.Date)value;
                value = new Timestamp(((Date) value).getTime());
             }
         }
         return super.set(attr, value);
     }
    }