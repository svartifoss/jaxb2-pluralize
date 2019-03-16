package is.svartifoss.jaxb2.plugin.pluralize;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.outline.Outline;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@ExtendWith(MockitoExtension.class)
public class PluralizePluginUnitTest {

    private PluralizePlugin pluralizePlugin;

    @Mock
    private Outline outline;

    @Mock
    private Options options;

    @Mock
    private ErrorHandler errorHandler;

    @Mock
    private Model model;

    @Mock
    private NClass nClass;

    @Mock
    private CClassInfo cClassInfo;

    @Mock
    private CPropertyInfo cPropertyInfo;

    @BeforeEach
    void setUp() {
        pluralizePlugin = new PluralizePlugin();
    }

    @Test
    public void theRunMethodIsEffectivelyANoOp() throws SAXException {
        final boolean result = pluralizePlugin.run(outline, options, errorHandler);

        assertThat(result).isTrue();
        verifyZeroInteractions(outline);
        verifyZeroInteractions(options);
        verifyZeroInteractions(errorHandler);
    }

    @Test
    public void thePluginReportsTheExpectedOptionName() {
        final String optionName = pluralizePlugin.getOptionName();
        assertThat(optionName).isEqualTo("Xpluralize");
    }

    @Test
    public void thePluginReportsTheExpectedUsage() {
        final String usage = pluralizePlugin.getUsage();
        assertThat(usage).isEqualTo("\t-Xpluralize\tenable");
    }

    @Test
    public void thePlugin() {
        Mockito.when(cPropertyInfo.isCollection()).thenReturn(true);
        Mockito.when(cPropertyInfo.getName(true)).thenReturn("Element");
        Mockito.when(cPropertyInfo.getName(false)).thenReturn("element");
        Mockito.when(cClassInfo.getProperties()).thenReturn(singletonList(cPropertyInfo));
        Mockito.when(model.beans()).thenReturn(singletonMap(nClass, cClassInfo));

        pluralizePlugin.postProcessModel(model, errorHandler);

        verifyZeroInteractions(errorHandler);
        verify(cPropertyInfo).setName(true, "Elements");
        verify(cPropertyInfo).setName(false, "elements");
    }
}
