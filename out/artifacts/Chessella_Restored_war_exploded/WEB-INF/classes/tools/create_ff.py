str = "java -jar ~/dev_tools/fernflower.jar classes/"
import glob
gl = glob.glob("lib/*.jar")
for g in gl:
    str = str + " -e="+g
#str = str + " -e=lib/hibernate2.jar -e=lib/commons-beanutils.jar -e=lib/commons-codec.jar -e=lib/commons-collections.jar -e=lib/commons-dbcp.jar -e=lib/commons-digester.jar -e=lib/commons-httpclient.jar -e=lib/commons-io.jar -e=lib/commons-lang.jar -e=lib/commons-logging.jar -e=lib/commons-pool.jar -e=lib/commons-validator.jar -e=lib/dwr.jar -e=lib/freemarker.jar -e=lib/hibernate-tools.jar -e=lib/jakarta-oro-2.0.8.jar" 
str = str + " source/"
print(str)
