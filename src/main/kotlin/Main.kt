import java.time.LocalDate
import java.time.format.DateTimeParseException

interface Person {
    val name: String
    val phone: String
}
//
data class Borrower(val libraryCard: String, override val name: String, override val phone: String) : Person

enum class BookStatus {
    Available,
    ON_Loan
}

enum class MainMenuOption {
    AddBook,
    RemoveBook,
    DisplayAllBooks,
    CreateLoan,
    DisplayAllLoans,
    ExitApp
}

data class Book(val title: String, val author: String, val publicationDate: LocalDate)

data class LibraryBook(val book: Book, var status: BookStatus)

data class Loan(val book: Book, val borrower: Borrower, val dueDate: LocalDate, var returnDate: LocalDate?)

class Library {
    private val books = mutableListOf<LibraryBook>()
    private val loans = mutableListOf<Loan>()

    fun addBook(book: Book) {
        val libraryBook = LibraryBook(book, BookStatus.Available)
        books.add(libraryBook)
    }

    fun removeBook(book: Book) {
        val libraryBook = books.find { it.book == book }
        libraryBook?.let {
            books.remove(libraryBook)
        }
    }

    fun displayAllBooks() {
        println("List of All Books in Cyber-Library:")
        books.forEach {
            println("- ${it.book.title} by ${it.book.author} (${it.book.publicationDate.year}) [${it.status}]")
        }
    }

    fun createLoan(borrower: Borrower, book: Book, dueDate: LocalDate) {
        val libraryBook = books.find { it.book == book }
        if (libraryBook != null && libraryBook.status == BookStatus.Available) {
            libraryBook.status = BookStatus.ON_Loan
            val loan = Loan(book, borrower, dueDate, null)
            loans.add(loan)
            println("Loan created: ${borrower.name} borrowed ${book.title}")
        } else {
            println("Sorry the Book is not available for loan")
        }
    }

    fun returnBook(book: Book) {
        val libraryBook = books.find { it.book == book }
        val loan = loans.find { it.book == book && it.returnDate == null }
        if (libraryBook != null && loan != null) {
            libraryBook.status = BookStatus.Available
            loan.returnDate = LocalDate.now()
            println("Book returned: ${book.title}")
        } else {
            println("Book is not on loan or could not be found")
        }
    }

    fun displayAllLoans() {
        println("All loans:")
        loans.forEach {
            println("- ${it.borrower.name} borrowed ${it.book.title} (due: ${it.dueDate}, returned: ${it.returnDate ?: "not returned"})")
        }
    }
}

class LibraryLoan(private val loans: List<Loan>) {
    fun getOverdueLoans(today: LocalDate): List<Loan> {
        return loans.filter { it.dueDate < today && it.returnDate == null }
    }

    fun getLoansByBorrower(borrower: Borrower): List<Loan> {
        return loans.filter { it.borrower == borrower }
    }
}



fun main() {
    val library = Library()


    println("Welcome to the Cyberking Library System!")

    while (true) {
        println(" ")
        println("Users Main Menu:")
        println("Please type the option for the following:")
        MainMenuOption.values().forEachIndexed { index, option ->
            println("${index + 1}. $option")

        }
        print(">:")
        val userInput = readLine() ?: continue

        try {
            val selectedOption = MainMenuOption.values()[userInput.toInt() - 1]

            when (selectedOption) {
                MainMenuOption.AddBook -> {
                    println("Enter book title:")
                    val title = readLine() ?: continue

                    println("Enter book author:")
                    val author = readLine() ?: continue

                    println("Enter book publication date (yyyy-MM-dd):")
                    val publicationDate = try {
                        LocalDate.parse(readLine())
                    } catch (e: DateTimeParseException) {
                        println("Invalid date format")
                        continue
                    }

                    val book = Book(title, author, publicationDate)
                    library.addBook(book)
                    println("Book added: $title")
                }

                MainMenuOption.RemoveBook -> {
                    println("\nEnter book title:")
                    val title = readLine() ?: continue

                    println("\nEnter book author:")
                    val author = readLine() ?: continue

                    println("\nEnter book publication date (yyyy-MM-dd):")
                    val publicationDate = try {
                        LocalDate.parse(readLine())
                    } catch (e: DateTimeParseException) {
                        println("\nInvalid date format!")
                        continue
                    }

                    val book = Book(title, author, publicationDate)
                    library.removeBook(book)
                    println("Book removed: $title")
                }

                MainMenuOption.DisplayAllBooks -> {
                    library.displayAllBooks()
                }

                MainMenuOption.CreateLoan -> {
                    println("Enter borrower library card number:")
                    val libraryCard = readLine() ?: continue

                    println("Enter borrower name:")
                    val borrowerName = readLine() ?: continue

                    println("Enter borrower phone number:")
                    val borrowerPhone = readLine() ?: continue

                    val borrower = Borrower(libraryCard, borrowerName, borrowerPhone)

                    println("Enter book title:")
                    val title = readLine() ?: continue

                    println("Enter book author:")
                    val author = readLine() ?: continue

                    println("Enter book publication date (yyyy-MM-dd):")
                    val publicationDate = try {
                        LocalDate.parse(readLine())
                    } catch (e: DateTimeParseException) {
                        println("Invalid date format")
                        continue
                    }

                    val book = Book(title, author, publicationDate)

                    println("Enter loan due date (yyyy-MM-dd):")
                    val dueDate = try {
                        LocalDate.parse(readLine())
                    } catch (e: DateTimeParseException) {
                        println("Invalid date format")
                        continue
                    }

                    library.createLoan(borrower, book, dueDate)
                }

                MainMenuOption.DisplayAllLoans -> {
                    library.displayAllLoans()
                }

                MainMenuOption.ExitApp -> {
                    println("Exiting Library System...")
                    return
                }
            }
        } catch (e: Exception) {
            println("Error Occur! Please Try Again!")
        }
    }
}


