package mark.component.dbmodel.model;

import java.util.List;

/**
 *
 * @author liang
 */
public class SystemFunction extends AbstractDatabaseObject {

    public static class Arg {

        private List<String> jdbcTypes;

        public Arg(List<String> jdbcTypes) {
            this.jdbcTypes = jdbcTypes;
        }

        public boolean isIsOptional() {
            return false;
        }

        public boolean isIsVaArg() {
            return false;
        }

        public List<String> getJdbcTypes() {
            return jdbcTypes;
        }
    }

    public static class OptionalArg extends Arg {

        public OptionalArg(List<String> jdbcTypes) {
            super(jdbcTypes);
        }

        @Override
        public boolean isIsOptional() {
            return true;
        }
    }

    /*
     * 可变参数的参数都是可选的
     */
    public static class VaArg extends OptionalArg {

        public VaArg(List<String> jdbcTypes) {
            super(jdbcTypes);
        }

        @Override
        public boolean isIsVaArg() {
            return true;
        }
    }
    private List<Arg> args;
    private String syntax;
    private String description;
    private List<String> examples;

    public SystemFunction(Schema schema, String name, List<Arg> args, String syntax, String description, List<String> examples) {
        super(schema, name);
        this.args = args;
        this.syntax = syntax;
        this.description = description;
        this.examples = examples;
    }

    public List<Arg> getArgs() {
        return args;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getExamples() {
        return examples;
    }

    public String getSyntax() {
        return syntax;
    }
}
