# list csv files
files <- list.files(pattern = "^times_.*\\.csv$")

# auto colors
colors <- rainbow(length(files))

# prepare empty plot
plot(1, type="n",
     xlab="n (operation index)", ylab="normalized cumulative time",
     xlim=c(0, 1), ylim=c(0, 1),
     main="TreeMap timings vs O(n log n)")

# add O(n log n) baseline
nmax <- 0
for (f in files) {
  df <- read.csv(f)
  nmax <- max(nmax, max(df$operation_index))
}
n <- seq(1, nmax, length.out=1000)
theoretical <- n * log(n)
theoretical <- theoretical / max(theoretical)
lines(n / max(n), theoretical, col="black", lty=2, lwd=2)

# helper to make pretty labels
make_label <- function(filename) {
  # remove "times_" prefix and ".csv" suffix
  base <- sub("^times_", "", filename)
  base <- sub("\\.csv$", "", base)
  # split by "__"
  parts <- strsplit(base, "__")[[1]]
  # we care about "query_heavy_100000_ops" part
  kind <- parts[length(parts)]
  # replace underscores with spaces
  kind <- gsub("_", " ", kind)
  return(kind)
}

# plot datasets
i <- 1
legend_labels <- c("O(n log n)")
legend_colors <- c("black")
legend_lty <- c(2)

for (f in files) {
  df <- read.csv(f)
  n <- df$operation_index
  time <- df$time_ms
  total_time <- cumsum(time)
  total_time_norm <- total_time / max(total_time)

  lines(n / max(n), total_time_norm, col=colors[i], lwd=1.5)
  legend_labels <- c(legend_labels, make_label(f))
  legend_colors <- c(legend_colors, colors[i])
  legend_lty <- c(legend_lty, 1)
  i <- i + 1
}

legend("topleft", legend=legend_labels,
       col=legend_colors, lty=legend_lty, lwd=2, cex=0.8)

