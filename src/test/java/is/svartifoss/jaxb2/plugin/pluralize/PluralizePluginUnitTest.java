package is.svartifoss.jaxb2.plugin.pluralize;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.outline.Outline;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PluralizePluginUnitTest {
    @Test
    public void theRunMethodIsEffectivelyANoOp() throws SAXException {
        final PluralizePlugin pluralizePlugin = new PluralizePlugin();
        final Outline outline = Mockito.mock(Outline.class);
        final Options options = Mockito.mock(Options.class);
        final ErrorHandler errorHandler = Mockito.mock(ErrorHandler.class);

        final boolean result = pluralizePlugin.run(outline, options, errorHandler);

        assertThat(result).isTrue();
        verifyZeroInteractions(outline);
        verifyZeroInteractions(options);
        verifyZeroInteractions(errorHandler);
    }

    @Test
    public void thePluginReportsTheExpectedOptionName() {
        final PluralizePlugin pluralizePlugin = new PluralizePlugin();
        final String optionName = pluralizePlugin.getOptionName();
        assertThat(optionName).isEqualTo("Xpluralize");
    }

    @Test
    public void thePluginReportsTheExpectedUsage() {
        final PluralizePlugin pluralizePlugin = new PluralizePlugin();
        final String usage = pluralizePlugin.getUsage();
        assertThat(usage).isEqualTo("\t-Xpluralize\tenable");
    }
}
