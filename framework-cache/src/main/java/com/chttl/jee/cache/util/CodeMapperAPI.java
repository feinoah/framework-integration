package com.chttl.jee.cache.util;

import org.springframework.util.MultiValueMap;

/**
 * MultiValueMap 可透過 MultiValueMapUtil 輔助使用
 * 
 * 		MultiValueMap<MultiValueKey,String> map = new LinkedMultiValueMap<MultiValueKey,String>();
		map.add(MultiValueKey.of("1"), "A");
		map.add(MultiValueKey.of("1"), "B");
		map.add(MultiValueKey.of("1"), "C");
		map.add(MultiValueKey.of("2"), "D");
		map.add(MultiValueKey.of("2"), "E");
		map.add(MultiValueKey.of("2"), "F");

		MultiValueMapUtil<String> util = MultiValueMapUtil.<String>of(map);
		String first = util.getSingle("1") ; //A
		List<String> all = util.getMulit("2"); //[D, E, F]
 *
 */
public interface CodeMapperAPI {

	//key: code2name#entityName#codeFieldName#valueFieldName
	public <T> String code2Name(Class<T> entity,String codeFieldName,String valueFieldName,String code)  ;
	
	//key: code2name#entityName#codeFieldName#...#valueFieldName,...
	public <T> String code2Name(Class<T> entity,String[] codeFieldNames,String valueFieldName, String[] codes)  ;
	
	//key: gettable#entityName#keyFieldName
	public <T> MultiValueMap<MultiValueKey,Object> getTable(Class<T> entity, String keyFieldName)  ;
	
	//key: gettable#entityName#keyFieldName,...
	public <T> MultiValueMap<MultiValueKey,Object> getTable(Class<T> entity, String... keyFieldNames) ;

	//key: getcodetable#entityName#codeFieldName#valueFieldName
	public <T> MultiValueMap<MultiValueKey,String> getCodeTable(Class<T> entity,String codeFieldName,String valueFieldName)  ;
	
	//key: getcodetable#entityName#codeFieldName#...#valueFieldName
	public <T> MultiValueMap<MultiValueKey,String> getCodeTable(Class<T> entity,String[] codeFieldNames,String valueFieldName)  ;
	

}
