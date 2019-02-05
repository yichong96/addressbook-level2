package seedu.addressbook.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.person.ReadOnlyPerson;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords or whose keywords are
 * substrings of names.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    /**
     * Returns a copy of keywords in this command.
     */
    public Set<String> getKeywords() {
        return new HashSet<>(keywords);
    }

    @Override
    public CommandResult execute() {
        return CommandResultForFindSuccessOrFail();
    }


    /**
     * Returns a different CommandResult message based on whether keywords are substrings of names or whether keyword is
     * in names.
     * Substring method only works for search on a single name.
     * @return CommandResult with a different message
     */
    private CommandResult CommandResultForFindSuccessOrFail() {
        List<ReadOnlyPerson> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
        List<ReadOnlyPerson> similarPersonsFound = getPersonsWithNameContainingSubstringOfKeyword(keywords);
        if (personsFound.isEmpty() && !similarPersonsFound.isEmpty()) {
            return new CommandResult(Messages.MESSAGE_MEANT_TO_FIND + getMessageForPersonListShownSummary(similarPersonsFound), similarPersonsFound);
        }
        return new CommandResult(getMessageForPersonListShownSummary(personsFound), personsFound);
    }


    /**
     * Retrieves all persons whose keywords are substrings of names.
     * @param keywords for searching
     * @return list of persons whose keywords are substrings of the names in addressbook.
     */
    private List<ReadOnlyPerson> getPersonsWithNameContainingSubstringOfKeyword(Set<String> keywords) {
        final List<ReadOnlyPerson> matchedPersons = new ArrayList<>();
        for (ReadOnlyPerson person : addressBook.getAllPersons()) {
            final Set<String> wordsInName = new HashSet<>(person.getName().getWordsInName());
            if (containsSubstring(wordsInName, keywords)) {
                matchedPersons.add(person);
            }
        }
        return matchedPersons;
    }


    /**
     * Helper function to check if keywords are substrings of names in address book.
     * @param wordsInName names of people in the address book.
     * @param keywords  keywords for searching
     * @return  true if keywords are substring of any names in the address book.
     */
    private boolean containsSubstring(Set<String> wordsInName, Collection<String> keywords) {
        for (String keyword : keywords) {
            for (String word : wordsInName) {
                if (word.contains(keyword)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Retrieves all persons in the address book whose names contain some of the specified keywords.
     *
     * @param keywords for searching
     * @return list of persons found
     */
    private List<ReadOnlyPerson> getPersonsWithNameContainingAnyKeyword(Set<String> keywords) {
        final List<ReadOnlyPerson> matchedPersons = new ArrayList<>();
        for (ReadOnlyPerson person : addressBook.getAllPersons()) {
            final Set<String> wordsInName = new HashSet<>(person.getName().getWordsInName());
            if (!Collections.disjoint(wordsInName, keywords)) {
                matchedPersons.add(person);
            }
        }
        return matchedPersons;
    }
}
