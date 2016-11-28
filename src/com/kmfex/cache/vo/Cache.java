package com.kmfex.cache.vo;
public class Cache {   

        private String key;//缓存ID   

        private Object value;//缓存数据    
        
        //格式yyyyMMddHHmmsssss
        private long time;//缓存时间

        public Cache() {  
           super();  
        }   

        public Cache(String key, Object value) {  
           this.key = key;    
           this.value = value;  
        }   

  

        public Cache(String key, Object value, long time) { 
			this.key = key;
			this.value = value;
			this.time = time;
		}

        
        
		public String getKey() { 
          return key;  
        }   

  

  

        public Object getValue() { 
           return value;   
        }   

  

        public void setKey(String string) {  
            key = string;   
        }   


     
  

        public void setValue(Object object) {   
                value = object;  
        }

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}   
 
}   