package com.cache.oscache;

public class OSCache {
	/*
	GeneralCacheAdministrator admin;
    EntryRefreshPolicy policy =new ExpiresRefreshPolicy(900);

	public OSCache(){
		admin = new GeneralCacheAdministrator();
	}
	
	public OSCache(int size) {
		admin = new GeneralCacheAdministrator();
		admin.setCacheCapacity(size);
	}

	public void put(String key, String value) {
		this.admin.putInCache(key,value,policy);
	}
	
	public void put(String key,Object obj){
		this.admin.putInCache(key,obj,policy);
	}
	
	public void put(String key,String value,String[] groups){
		this.admin.putInCache(key,value,groups,policy);
	}
	
	public void put(String key,Object obj,String[] groups){
		this.admin.putInCache(key,obj,groups,policy);
	}
	
	
	public Object get(String key,String defaultValue,int myRefreshPeriod){
		try{
			return (Object)this.admin.getFromCache(key,myRefreshPeriod);
		}catch(Exception ex){
			this.admin.putInCache(key, defaultValue);
			return null;
		}
	}

	public Object get(String key,String defaultValue){
		try{
			return (Object)this.admin.getFromCache(key);
		}catch(NeedsRefreshException ex){
			this.admin.putInCache(key, defaultValue);
			return null;
		}
	}
*/
	/*
	 * 	class Award {//奖品类
	    public String id;
	    public float probability;
	    public int count;
	    public Award(String id, float probability, int count) {
	        super();
	        this.id = id;
	        this.probability = probability;
	        this.count = count;
	    }
	    public Award(){
	    }
	}
	class OSCache{//缓存类
		GeneralCacheAdministrator admin;
	    EntryRefreshPolicy policy =new ExpiresRefreshPolicy(900);
		public OSCache(){
			admin = new GeneralCacheAdministrator();
		}
		public OSCache(int size) {
			admin = new GeneralCacheAdministrator();
			admin.setCacheCapacity(size);
		}
		public void put(String key, String value) {
			this.admin.putInCache(key,value,policy);
		}
		public void put(String key,Object obj){
			this.admin.putInCache(key,obj,policy);
		}
		public void put(String key,String value,String[] groups){
			this.admin.putInCache(key,value,groups,policy);
		}
		public Object get(String key,String defaultValue){
			try{
				return (Object)this.admin.getFromCache(key);
			}catch(NeedsRefreshException ex){
				this.admin.putInCache(key, defaultValue);
				return null;
			}
		}
	}
	OSCache osCache = new OSCache(5);
	*/
}
