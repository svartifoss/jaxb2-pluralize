package is.svartifoss.jaxb2.plugin.pluralize;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;
import org.atteo.evo.inflector.English;
import org.xml.sax.ErrorHandler;

import java.util.Collection;

public class PluralizePlugin extends Plugin {

    private static final String OPTION_NAME = "Xpluralize";
    private static final String USAGE = "\t-Xpluralize\tenable";

    @Override
    public String getOptionName() {
        return OPTION_NAME;
    }

    @Override
    public String getUsage() {
        return USAGE;
    }

    @Override
    public void postProcessModel(Model model, ErrorHandler errorHandler) {
        model.beans()
             .values()
             .stream()
             .map(CClassInfo::getProperties)
             .flatMap(Collection::stream)
             .filter(CPropertyInfo::isCollection)
             .forEach(this::pluralize);
    }

    private void pluralize(final CPropertyInfo propertyInfo) {
        propertyInfo.setName(true, pluralize(propertyInfo.getName(true)));
        propertyInfo.setName(false, pluralize(propertyInfo.getName(false)));
    }

    private String pluralize(final String string) {
        return English.plural(string);
    }

    @Override
    public boolean run(final Outline outline, final Options options, final ErrorHandler errorHandler) {
        return true;
    }
}
