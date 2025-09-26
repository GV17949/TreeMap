# list csv files
files <- list.files(pattern = "^times_.*\\.csv$")

# separate files by tree type
avl_files <- files[grep("AVL_Tree", files)]
pv_files <- files[grep("PV_Tree", files)]
java_files <- files[grep("Java_TreeMap", files)]

# colors for each tree type
col_avl <- "blue"
col_pv <- "red"
col_java <- "black"

# helper to extract operation type
get_op_type <- function(filename) {
  if (grepl("query_heavy", filename)) {
    return("consultas")
  } else if (grepl("insert_delete_heavy", filename)) {
    return("inserção/remoção")
  }
  return(NA)  # Skip mixed workloads
}

# helper to extract operation count
get_op_count <- function(filename) {
  if (grepl("100000_ops", filename)) return("100k")
  if (grepl("10000_ops", filename)) return("10k")
  if (grepl("100_ops", filename)) return("100")
  return("")
}

# get unique operation counts
op_counts <- unique(c(
  sapply(avl_files, get_op_count),
  sapply(pv_files, get_op_count),
  sapply(java_files, get_op_count)
))
op_counts <- op_counts[op_counts != ""]
op_counts <- sort(op_counts)

# create subplot layout with bigger figure size
# Option 1: Make the entire plot window bigger
par(mfrow=c(1, length(op_counts)), mar=c(4, 4, 3, 1), fig=c(0,1,0,1))
# Increase overall plot size
dev.new(width=15, height=6)  # width=15 inches, height=6 inches

# plot each workload size separately
for (op_count in op_counts) {
  # prepare empty plot
  plot(1, type="n",
       xlab="n (índice da operação)", 
       ylab="tempo acumulado normalizado",
       xlim=c(0, 1), ylim=c(0, 1),
       main=paste("Workload:", op_count, "operações"))
  
  # filter files for current operation count
  current_java <- java_files[sapply(java_files, get_op_count) == op_count]
  current_avl <- avl_files[sapply(avl_files, get_op_count) == op_count]
  current_pv <- pv_files[sapply(pv_files, get_op_count) == op_count]
  
  # plot Java TreeMap as baseline (dashed lines)
  for (f in current_java) {
    workload_type <- get_op_type(f)
    if (is.na(workload_type)) next  # Skip mixed workloads
    
    df <- read.csv(f)
    n <- df$operation_index
    time <- df$time_ms
    total_time <- cumsum(time)
    total_time_norm <- total_time / max(total_time)
    
    # determine line type based on workload
    lty_style <- if(workload_type == "consultas") 2 else 3
    
    lines(n / max(n), total_time_norm, col=col_java, lty=lty_style, lwd=2)
  }
  
  # plot AVL trees
  for (f in current_avl) {
    workload_type <- get_op_type(f)
    if (is.na(workload_type)) next  # Skip mixed workloads
    
    df <- read.csv(f)
    n <- df$operation_index
    time <- df$time_ms
    total_time <- cumsum(time)
    total_time_norm <- total_time / max(total_time)
    
    # determine line type based on workload
    lty_style <- if(workload_type == "consultas") 1 else 3
    
    lines(n / max(n), total_time_norm, col=col_avl, lty=lty_style, lwd=2)
  }
  
  # plot PV trees
  for (f in current_pv) {
    workload_type <- get_op_type(f)
    if (is.na(workload_type)) next  # Skip mixed workloads
    
    df <- read.csv(f)
    n <- df$operation_index
    time <- df$time_ms
    total_time <- cumsum(time)
    total_time_norm <- total_time / max(total_time)
    
    # determine line type based on workload
    lty_style <- if(workload_type == "consultas") 1 else 3
    
    lines(n / max(n), total_time_norm, col=col_pv, lty=lty_style, lwd=2)
  }
  
  # add legend to all subplots
  legend_labels <- c(
    "Java TreeMap - Consultas",
    "Java TreeMap - Inserção/Remoção", 
    "Árvore AVL - Consultas",
    "Árvore AVL - Inserção/Remoção",
    "Árvore PV - Consultas",
    "Árvore PV - Inserção/Remoção"
  )
  legend_colors <- c(col_java, col_java, col_avl, col_avl, col_pv, col_pv)
  legend_lty <- c(2, 3, 1, 3, 1, 3)
  legend("topleft", 
         legend=legend_labels,
         col=legend_colors, 
         lty=legend_lty, 
         lwd=2, 
         cex=0.7,
         bg="white")
}

# reset plot layout
par(mfrow=c(1,1))
