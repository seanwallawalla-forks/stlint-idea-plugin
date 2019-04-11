package org.edadeal.settings;

import com.intellij.lang.javascript.linter.JSLinterConfigFileUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class StLintUtil {
    private StLintUtil() {
    }

    public static final Logger LOG = Logger.getInstance("#org.edadeal.stLint");
    public static final String PACKAGE_NAME = "stlint";
    public static final String STLINT_JSON = "stlint.json";

    public static final String[] CONFIG_FILE_NAMES = new String[]{STLINT_JSON, "stlint.yaml", "stlint.yml"};

    public static boolean isConfigFile(@NotNull VirtualFile file) {
        if (!file.isValid() || file.isDirectory()) {
            return false;
        }
        CharSequence name = file.getNameSequence();
        for (String fileName : CONFIG_FILE_NAMES) {
            if (StringUtil.equals(name, fileName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasConfigFiles(@NotNull Project project) {
        return JSLinterConfigFileUtil.hasConfigFiles(project, CONFIG_FILE_NAMES);
    }

    @NotNull
    public static List<VirtualFile> findAllConfigsInScope(@NotNull Project project) {
        return JSLinterConfigFileUtil.findAllConfigs(project, CONFIG_FILE_NAMES);
    }

    @Nullable
    public static VirtualFile getConfig(@NotNull StLintState state, @NotNull VirtualFile virtualFile) {
        if (state.isCustomConfigFileUsed()) {
            final String configFilePath = state.getCustomConfigFilePath();
            if (StringUtil.isEmptyOrSpaces(configFilePath)) {
                return null;
            }
            final File configFile = new File(configFilePath);
            return VfsUtil.findFileByIoFile(configFile, false);
        }

        return lookupConfig(virtualFile);
    }

    @Nullable
    public static VirtualFile lookupConfig(@NotNull VirtualFile virtualFile) {
        return JSLinterConfigFileUtil.findFileUpToFileSystemRoot(virtualFile, CONFIG_FILE_NAMES);
    }
}