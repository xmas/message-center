dat <- readLines(paste(dir, "input/input.txt", sep="/"))
png(paste(dir, "output/output.png", sep="/"), width = 800, height = 1100)
barplot(table(dat))
dev.off()