package cn.itcast.bigdata.hadooprpc.protocol;

public interface ClientNamenodeProtocol {
//	public static final long versionID=1L;
	public String getMetaData(String path);
}
