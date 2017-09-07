package tech.lihx.demo.core.support.privilege;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * 扫描Controller
 * <p>
 * 
 * @author lihx
 * @Date 2017-9-5 10:16:28
 */
public class ControllerScanner implements InitializingBean {

	static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

	private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private final MetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(
			this.resourcePatternResolver);

	private StorePrivilege store;

	private String[] basePackages;

	private String basePackage;


	protected String resolveBasePackage( @SuppressWarnings( "hiding" ) String basePackage ) {
		StandardServletEnvironment environment = new StandardServletEnvironment();
		return ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage));
	}


	public List<PrivilegeBean> scan() throws IOException {
		if ( basePackages == null ) {
			basePackages = new String[ ] { basePackage };
		}
		List<PrivilegeBean> list = new ArrayList<PrivilegeBean>();
		for ( int i = 0 ; i < basePackages.length ; i++ ) {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ resolveBasePackage(basePackages[i]) + "/" + DEFAULT_RESOURCE_PATTERN;
			Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
			for ( Resource resource : resources ) {
				if ( resource.isReadable() ) {
					MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
					AnnotationMetadata metaClass = metadataReader.getAnnotationMetadata();
					// 是控制器
					if ( metaClass.hasAnnotation(Controller.class.getName()) ) {
						String[] uri = getUri(metaClass);
						// 是可以请求的方法
						if ( metaClass.hasAnnotatedMethods(RequestMapping.class.getName()) ) {
							Set<MethodMetadata> meta = metaClass.getAnnotatedMethods(RequestMapping.class.getName());
							for ( MethodMetadata methodMetadata : meta ) {
								Map<String, Object> attr = methodMetadata.getAnnotationAttributes(Privilege.class
										.getName());
								if ( attr == null || attr.isEmpty() ) { throw new RuntimeException(
										methodMetadata.getDeclaringClassName()
												+ "中的" + methodMetadata.getMethodName()
												+ "方法没有加Privilege注解,请添加,至少加入描述信息"); }
								PrivilegeBean bean = new PrivilegeBean();
								bean.setClassName(methodMetadata.getDeclaringClassName());
								bean.setMethodName(methodMetadata.getMethodName());
								bean.setUri(getUri(uri, methodMetadata));
								bean.setParent((String) attr.get("parent"));
								if ( attr.get("key").equals("") ) {
									bean.setKey(methodMetadata.getDeclaringClassName()
											+ "." + methodMetadata.getMethodName());
								} else {
									bean.setKey((String) attr.get("key"));
								}
								bean.setControl((Boolean) attr.get("control"));
								bean.setDesc((String) attr.get("value"));
								list.add(bean);
							}
						}

					}

				}
			}
		}

		return list;
	}


	private String[] getUri( AnnotationMetadata metaClass ) {
		if ( metaClass.hasAnnotation(RequestMapping.class.getName()) ) {
			String[] uris = (String[]) metaClass.getAnnotationAttributes(RequestMapping.class.getName()).get("value");
			if ( uris.length == 0 ) {
				uris = new String[ ] { "" };
			}
			return uris;
		}
		return new String[ ] { "" };

	}


	private String[] getUri( String[] baseUrl, MethodMetadata methodMetadata ) {
		String[] uris = (String[]) methodMetadata.getAnnotationAttributes(RequestMapping.class.getName()).get("value");
		if ( uris.length == 0 ) {
			uris = new String[ ] { methodMetadata.getMethodName() };
		}
		List<String> result = new ArrayList<String>();
		for ( int i = 0 ; i < baseUrl.length ; i++ ) {
			if ( !baseUrl[i].endsWith("/") ) {
				baseUrl[i] += "/";
			}
			for ( int j = 0 ; j < uris.length ; j++ ) {
				if ( uris[j].startsWith("/") ) {
					uris[j].substring(1, uris[j].length());
				}
				result.add(baseUrl[i] + uris[j]);
			}
		}
		return result.toArray(new String[ ] {});

	}


	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(store, "store不能为null");
		List<PrivilegeBean> list = scan();
		store.insertOrUpdate(list);
	}


	public StorePrivilege getStore() {
		return store;
	}


	public void setStore( StorePrivilege store ) {
		this.store = store;
	}


	public String[] getBasePackages() {
		return basePackages;
	}


	public void setBasePackages( String[] basePackages ) {
		this.basePackages = basePackages;
	}


	public String getBasePackage() {
		return basePackage;
	}


	public void setBasePackage( String basePackage ) {
		this.basePackage = basePackage;
	}

}
