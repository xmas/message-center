for (i in 1:k) {
  png(sprintf(paste(WD, "Rplot%03d.png", sep="/"), i))
  barplot(table(sample(LETTERS[1:6], 100, rep=TRUE)))
  dev.off()
}